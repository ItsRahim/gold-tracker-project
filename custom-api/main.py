import uvicorn

from fastapi import FastAPI, HTTPException
from starlette.routing import Route
from starlette.requests import Request
from app.api.endpoints import price_router
from app.config.load_config import load_config
from app.database.db_manager import DatabaseManager

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

    db_manager = DatabaseManager()
    result = db_manager.execute_query("SELECT * FROM user_profiles")
    print(result)

    db_manager.close_connection()
    uvicorn.run(app, host=host, port=port)
