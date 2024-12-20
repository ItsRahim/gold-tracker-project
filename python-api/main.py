import uvicorn
from fastapi import FastAPI, HTTPException
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse
from starlette.requests import Request
from starlette.routing import Route

from app.api.encryptor import encryptor_router
from app.api.price_endpoint import price_router
from app.config.load_config import load_config, Config

config = load_config('app')

API_PREFIX = "/api/v1"
DEPLOYMENT_TYPE = Config.get_deployment_type()
app = FastAPI()

app.include_router(price_router, prefix=API_PREFIX)

if DEPLOYMENT_TYPE == "dev":
    app.include_router(encryptor_router, prefix=API_PREFIX)


@app.exception_handler(HTTPException)
async def http_exception_handler(request, exc):
    return JSONResponse(status_code=exc.status_code, content={"message": exc.detail})


@app.exception_handler(RequestValidationError)
async def validation_exception_handler(request, exc):
    return JSONResponse(status_code=400, content={"message": "Validation error", "details": exc.errors()})


async def catch_all(request: Request):
    raise HTTPException(status_code=404, detail=f'Endpoint {request.method} {request.url.path} not found')


catch_all_route = Route("/{path:path}", catch_all, methods=["GET", "POST", "PUT", "DELETE"])
app.router.routes.append(catch_all_route)

if __name__ == "__main__":
    host = config['host']
    port = config['port']

    uvicorn.run(app, host=host, port=port)
