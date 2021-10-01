from PortfolioRunner.StockActions.ActionTypes import Action
from PortfolioRunner.StockActions.StockAction import StockAction


class StockHolding:

    def __init__(self, ticker: str):
        self.ticker = ticker
        self.num_of_shares = 0
        self.collected_dividends = 0
        self.cost_basis = 0
        self.sold_shares = 0
        self.total_price_sold = 0
        self.total_cot_basis_of_sold_shares = 0
        self.current_share_price = 0
        self.actions: [StockAction] = []

    def add_action_on_stock(self, action: StockAction):
        self.actions.append(action)
        if action.action is Action.BUY:
            self._add_buy_action(action)
        elif action.action is Action.SELL:
            self._add_sell_action(action)
        elif action.action is Action.DIVIDEND:
            self._add_dividend_action(action)

    def _add_buy_action(self, action: StockAction):
        self.num_of_shares += action.quantity
        self.cost_basis += (action.quantity * action.price)

    def _add_sell_action(self, action: StockAction):
        self.sold_shares += action.quantity
        self.total_price_sold += (action.quantity * action.price)
        # //remove first bought shares
        # sorted(self.actions, key=operator.attrgetter('date'))
    #     see what the gain loss was

    def _add_dividend_action(self, action: StockAction):
        self.collected_dividends += action.amount
