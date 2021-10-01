import requests
from datetime import date, datetime, timedelta

today_date = date.today()
day_of_week = today_date.weekday()
var = today_date.day
print("Today date is: ", today_date)

today = datetime.today() #(2019, 9, 9)

if today.isoweekday() > 5:
    offset = max(1, (today.weekday() + 6) % 7 - 3) #Equation to find time elapsed.
    timedelta = timedelta(offset)
    most_recent = today - timedelta #. Subtract two datetime objects.
    print(most_recent.date())
else:
    print("today is a weekend")