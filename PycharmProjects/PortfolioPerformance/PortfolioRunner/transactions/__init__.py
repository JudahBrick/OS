import pandas as pd

from PortfolioRunner.transactions.Portfolio import Portfolio
from PortfolioRunner.transactions.StockHolding import StockHolding

transactions = pd.read_csv('/Users/yehudabrick/PycharmProjects/PortfolioPerformance/PortfolioRunner/transactions/Stock_Picks.CSV')
print(transactions.columns)
print(transactions.dtypes)
# read schedule tab into a panda dataframe
# transactions = transactions.drop(
#     columns=["Date", "Action", "Symbol", "Description", "Quantity", "Price", "Fees & Comm", "Amount"])
# transactions = transactions.dropna()

set_of_dates = set()
portfolio = Portfolio()
for index, row in transactions.iterrows():
    portfolio.add_record_from_csv(row)

tickers = portfolio.stocks.keys()
# 1 get prices for all of the stocks in the set of tickers
# 2 get the total percent gain for each stock
# 3 get total gain/loss for whole portfolio

# 4 get prices of spy / or index at each date in list of dates
# 5 go through each stock action and see the percentage gain/loss of the action
# 6 check percentage gain of SPY during that time frame
# 7 maybe also check dividends (probably another time diff alg for total return)
# 8 check how much the money that was put into that action would have made in spy
# 9 compare the 2 totals


for ticker in tickers:
    print(ticker + " Num of ")
    stock: StockHolding = portfolio.stocks[ticker]
    print(stock.num_of_shares)

x = 20
y = 90
print(str(x + y))



