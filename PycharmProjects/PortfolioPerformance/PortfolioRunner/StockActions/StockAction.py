from PortfolioRunner.StockActions.ActionTypes import Action, get_action_type
from PortfolioRunner.transactions import SimpleDate


def clean_number_of_non_digits(number: str) -> float:
    try:
        if number.isdigit():
            return float(number)
        elif number is None:
            return 0.0
        else:
            number = number.replace("$", "")
        return float(number)
    except AttributeError as err:
        print("----error----")
        print(number)
        print(err)


class StockAction:

    def __init__(self, ticker: str, action: str, quantity: float, price: str, amount: str, date: SimpleDate):
        self.ticker = ticker
        self.action: Action = get_action_type(action)
        if self.action is Action.DIVIDEND:
            self.dividend = action
            self.amount: float = clean_number_of_non_digits(amount)
        else:
        # TODO need to strip these strings of the dollar signs
            self.quantity: float = quantity
            self.price: float = clean_number_of_non_digits(price)
        self.action_date = date

    def __str__(self):
        rtn: str = self.ticker + " " + \
                    str(self.action.name) + " " +\
                    "date: " + str(self.action_date) + " "
        if self.action is Action.DIVIDEND:
            rtn += "amount: " + str(self.amount) + " " + \
                   " dividend type: " + self.dividend
        else:
            rtn += "quantity: " + str(self.quantity) + " " + \
                    "price: " + str(self.price)
        return rtn

