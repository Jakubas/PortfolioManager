# Portfolio Manager

Hosted on AWS: [TBC](www.example.com)

The project is currently undergoing refactoring.

This project is a portfolio management web application.

# Features

### Portfolio
Users can add shares to their portfolio from a list of companies. 
Currently this list consists of companies from the S&P 500 index. 
Other financial instruments such as bonds or options aren't supported at the moment.
After adding shares, users can view details about their portfolio such as the current value, annualised return, sector weighting, etc.
The portfolio consists of multiple buy/sell events and the user can view metrics about each event separately.

### Metrics
Besides the metrics that the user can view in the Portfolio view, there is also a seperate view dedicated to metrics.
This view has three distinct elements:
- Portfolio weighting by sector - a pie chart that shows your weighting in each market sector
- Portfolio value over time - a graph that shows the worth of your portfolio since your first stock purchase. This value is also compared to the S&P 500 Index peformance over the same timeframe so that the use can quickly see how their portfolio performed compared to a popular market index.
- Transaction history - a table showing the buy/sell events that occured and some details about them such as date, transacation value, etc.

### Goals
The user can set goals that they want to achieve. 
Currently 7 types of goals are supported:
- "I want _x_% of my stock in the _y_ sector"
- "I want to move _x_% of my holdings in _y_ to _z_"
- "I want to retire in _x_ years"
- "I want to grow my investments to $_x_"
- "I want to invest for _x_ (months/years)"
- "I want to have a _x_ risk portfolio"
- "I want to deposit $_x_ each month and grow my investments to $_y_"

Based on portfolio metrics and the goals set by the user I offer suggestions on how the user can proceed to fulfill these goals.
When a user sets a goal they will recieve advice on how achieve their given goal and/or have a progress bar showing their goal progress.
For example if a user sets a goal such as "I want to grow my investments to $_x_", it will tell the user how many more years and months until their goal is achieved, based on their portfolio's historical performance.

### Tracker
There is a tracker where a user can add stocks that want to keep an eye on. All tracked stocks are conveniently located in the same view, and this can be treated as a subset of the Stocks page that lists all the stocks supported by this application.

### User
New users have to register before they can use most of the features of the application. There is no automated recovery process at the moment, so you will need to create a new account if you don't remember the login details of the old one.

