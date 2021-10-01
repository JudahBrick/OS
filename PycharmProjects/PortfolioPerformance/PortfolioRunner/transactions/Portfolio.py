from pandas import Series

from PortfolioRunner.StockActions.StockAction import StockAction
from PortfolioRunner.transactions.SimpleDate import SimpleDate
from PortfolioRunner.transactions.StockHolding import StockHolding


def _get_action_from_row(row: Series) -> StockAction:
    action: StockAction = StockAction(ticker=row['Symbol'], action=row['Action'], quantity=row['Quantity'],
                                      price=row['Price'], amount=row['Amount'], date=SimpleDate(row["Date"]))
    print(action)
    return action


class Portfolio:

    def __init__(self):
        self.stocks: {str, StockHolding} = {}
        self.total_cost_basis = 0
        self.set_of_dates = set()

    def _add_stock_record(self, action: StockAction) -> None:
        ticker: str = action.ticker
        if self.stocks.__contains__(ticker) is True:
            stock: StockHolding = self.stocks.get(ticker)
            stock.add_action_on_stock(action)
        else:
            holding: StockHolding = StockHolding(ticker)
            holding.add_action_on_stock(action)
            self.stocks[ticker] = holding

    def add_record_from_csv(self, row: Series) -> None:
        try:
            date = row["Date"]
            self.set_of_dates.add(date)
            action: StockAction = _get_action_from_row(row)
            self._add_stock_record(action)
        except KeyError as err:
            print('there was an error')
