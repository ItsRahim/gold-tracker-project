import uvicorn
from fastapi import FastAPI, HTTPException
from starlette.requests import Request
from starlette.routing import Route

from app.api.endpoints import price_router
from app.config.load_config import load_config

app = FastAPI()
app.include_router(price_router, prefix="/api/v1/gold")
config = load_config('app')


async def catch_all(request: Request):
    raise HTTPException(status_code=404, detail=f'Endpoint {request.method} {request.url.path} not found')

catch_all_route = Route("/{path:path}", catch_all, methods=["GET", "POST", "PUT", "DELETE"])
app.router.routes.append(catch_all_route)


if __name__ == "__main__":
    host = config['host']
    port = config['port']

    uvicorn.run(app, host=host, port=port)
