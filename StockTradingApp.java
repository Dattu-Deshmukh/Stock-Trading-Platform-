import java.util.*;

class Stock {
    private String symbol;
    private String name;
    private double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return symbol + " (" + name + ") - $" + price;
    }
}

class Portfolio {
    private double cash;
    private Map<String, Integer> holdings;
    private List<String> transactionHistory;

    public Portfolio(double initialCash) {
        this.cash = initialCash;
        this.holdings = new HashMap<>();
        this.transactionHistory = new ArrayList<>();
    }

    public double getCash() {
        return cash;
    }

    public void depositCash(double amount) {
        cash += amount;
        transactionHistory.add("Deposited $" + amount);
    }

    public void buyStock(Stock stock, int quantity) {
        double cost = stock.getPrice() * quantity;
        if (cost > cash) {
            System.out.println("Insufficient cash to buy " + quantity + " shares of " + stock.getSymbol());
            return;
        }
        cash -= cost;
        holdings.put(stock.getSymbol(), holdings.getOrDefault(stock.getSymbol(), 0) + quantity);
        transactionHistory.add("Bought " + quantity + " shares of " + stock.getSymbol() + " at $" + stock.getPrice());
    }

    public void sellStock(Stock stock, int quantity) {
        int currentHolding = holdings.getOrDefault(stock.getSymbol(), 0);
        if (quantity > currentHolding) {
            System.out.println("Not enough shares to sell.");
            return;
        }
        cash += stock.getPrice() * quantity;
        holdings.put(stock.getSymbol(), currentHolding - quantity);
        transactionHistory.add("Sold " + quantity + " shares of " + stock.getSymbol() + " at $" + stock.getPrice());
    }

    public void viewPortfolio(Map<String, Stock> marketData) {
        System.out.println("Portfolio Summary:");
        System.out.println("Cash Balance: $" + cash);
        System.out.println("Holdings:");
        double totalValue = cash;
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            Stock stock = marketData.get(entry.getKey());
            double value = stock.getPrice() * entry.getValue();
            System.out.println(entry.getKey() + ": " + entry.getValue() + " shares @ $" + stock.getPrice() + " = $" + value);
            totalValue += value;
        }
        System.out.println("Total Portfolio Value: $" + totalValue);
    }

    public void viewTransactionHistory() {
        System.out.println("Transaction History:");
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }
}

class Market {
    private Map<String, Stock> stocks;

    public Market() {
        stocks = new HashMap<>();
        // Adding some sample stocks to the market
        stocks.put("AAPL", new Stock("AAPL", "Apple Inc.", 150.0));
        stocks.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 2800.0));
        stocks.put("AMZN", new Stock("AMZN", "Amazon.com Inc.", 3400.0));
    }

    public void simulateMarket() {
        // Simulating market by randomly changing stock prices
        Random random = new Random();
        for (Stock stock : stocks.values()) {
            double change = (random.nextDouble() * 2 - 1) * 5;  // Random change between -5 and +5
            stock.setPrice(stock.getPrice() + change);
        }
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public void showMarketData() {
        System.out.println("Current Market Data:");
        for (Stock stock : stocks.values()) {
            System.out.println(stock);
        }
    }
}

class TradingPlatform {
    private Market market;
    private Portfolio portfolio;
    private Scanner scanner;

    public TradingPlatform() {
        this.market = new Market();
        this.portfolio = new Portfolio(10000.0);  // Initial cash balance of $10,000
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n--- Stock Trading Platform ---");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Simulate Market");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    market.showMarketData();
                    break;
                case 2:
                    buyStock();
                    break;
                case 3:
                    sellStock();
                    break;
                case 4:
                    portfolio.viewPortfolio(market.getMarketData());
                    break;
                case 5:
                    portfolio.viewTransactionHistory();
                    break;
                case 6:
                    market.simulateMarket();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void buyStock() {
        System.out.print("Enter stock symbol to buy: ");
        String symbol = scanner.next().toUpperCase();
        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("Invalid stock symbol.");
            return;
        }
        System.out.print("Enter quantity to buy: ");
        int quantity = scanner.nextInt();
        portfolio.buyStock(stock, quantity);
    }

    private void sellStock() {
        System.out.print("Enter stock symbol to sell: ");
        String symbol = scanner.next().toUpperCase();
        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("Invalid stock symbol.");
            return;
        }
        System.out.print("Enter quantity to sell: ");
        int quantity = scanner.nextInt();
        portfolio.sellStock(stock, quantity);
    }
}

public class StockTradingApp {
    public static void main(String[] args) {
        TradingPlatform platform = new TradingPlatform();
        platform.start();
    }
}
