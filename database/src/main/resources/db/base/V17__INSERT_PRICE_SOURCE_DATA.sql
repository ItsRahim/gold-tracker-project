INSERT INTO rgts.price_sources (source_name, source_endpoint, source_url, source_element_data, source_is_active)
VALUES ('UK Investing', 'uk-investing', 'https://uk.investing.com/currencies/xau-gbp', '["div", {"data-test": "instrument-price-last"}]', TRUE);

INSERT INTO rgts.price_sources (source_name, source_endpoint, source_url, source_element_data, source_is_active)
VALUES ('Bloomberg', 'bloomberg', 'https://www.bloomberg.com/quote/XAUGBP:CUR', '["div", {"data-component": "sized-price", "class": "sized-price SizedPrice_extraLarge-05pKbJRbUH8-"}]', FALSE);

INSERT INTO rgts.price_sources (source_name, source_endpoint, source_url, source_element_data, source_is_active)
VALUES ('CNBC', 'cnbc', 'https://www.cnbc.com/quotes/XAUGBP=', '["span", {"class": "QuoteStrip-lastPrice"}]', FALSE);

INSERT INTO rgts.price_sources (source_name, source_endpoint, source_url, source_element_data, source_is_active)
VALUES ('Forbes', 'forbes', 'https://www.forbes.com/advisor/money-transfer/currency-converter/xau-gbp/', '["span", {"class": "amount"}]', FALSE);

INSERT INTO rgts.price_sources (source_name, source_endpoint, source_url, source_element_data, source_is_active)
VALUES ('Gold UK', 'gold-uk', 'https://www.cnbc.com/quotes/XAUGBP=', '["span", {"class": "QuoteStrip-lastPrice"}]', FALSE);
