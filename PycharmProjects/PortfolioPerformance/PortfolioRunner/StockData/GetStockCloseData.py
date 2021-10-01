import requests
from requests import Response

params = {
  'access_key': '8e6ee4a332d12b6738adca06b7356f42',
}
get_ticker_data_uri = 'http://api.marketstack.com/v1/tickers/'
eod_string = '/eod'


def get_end_of_day_data(ticker: str, date: str) -> Response:
    request_string: str = get_ticker_data_uri + ticker + eod_string + date
    api_result: Response = requests.get(request_string, params)
    return api_result.json()


