
class SimpleDate:
    def __init__(self, date: str):
        self._date_string = date
        date_split = date.split('/')
        self.year = date_split[2]
        self.day = date_split[1]
        self.month = date_split[0]

    def __str__(self):
        return self.month + "-" + self.day + "-" + self.year


