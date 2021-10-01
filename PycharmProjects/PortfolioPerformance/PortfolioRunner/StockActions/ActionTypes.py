from enum import Enum


class Action(Enum):
    BUY = 1
    SELL = 2
    DIVIDEND = 3


def get_action_type(action: str) -> Action:
    if action == "Buy":
        return Action.BUY
    elif action == "Sell":
        return Action.SELL
    elif action.__contains__("Div"):
        return Action.DIVIDEND
