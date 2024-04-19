from app.config.logging import log
from app.database.db_manager import DatabaseManager


def get_source(requested_source: str) -> dict or None:
    """
    Retrieve a source from the database based on the provided source endpoint.
    Args:
        requested_source (str): The source endpoint to search for.
    Returns:
        dict or None: A dictionary containing the source details if found, None otherwise.
    """
    query = (
        "SELECT source_name, source_url, source_element_data "
        "FROM rgts.price_sources "
        "WHERE source_endpoint = :requested_source "
        "AND source_is_active = :active"
    )

    fallback_source_endpoint = 'uk-investing'
    connection = DatabaseManager()

    try:
        params = {'requested_source': requested_source, 'active': 'true'}
        source = connection.execute_query(query, params)

        if source and len(source) == 1:
            log.debug(f"Found valid requested source: {requested_source}")
            return source[0]
        else:
            log.warning(f"Requested source '{requested_source}' endpoint not found, inactive or not unique. "
                        f"Using default source: '{fallback_source_endpoint}'")

            params = {'requested_source': fallback_source_endpoint, 'active': 'true'}
            fallback_source = connection.execute_query(query, params)
            return fallback_source[0]

    except Exception as e:
        log.error(f"Error occurred while retrieving source: {e}")
        return None
