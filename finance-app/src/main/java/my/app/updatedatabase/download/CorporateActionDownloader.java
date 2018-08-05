package my.app.updatedatabase.download;

import my.app.domains.stock.Stock;
import my.app.utilities.DownloadUtility;

import java.util.List;

public class CorporateActionDownloader {

    public static void downloadCorporateActions(List<Stock> stocks, String rootDir) {
        int i = 1;
        for(Stock stock : stocks) {
            String ticker = stock.getTicker();
            String filePath = rootDir + ticker + "_actions.csv";
            CorporateActionDownloader.downloadCorporateAction(ticker, filePath);
            System.out.println(i++ + "/" + stocks.size() + " corporate actions downloaded");
        }
    }

    private static void downloadCorporateAction(String ticker, String filePath) {
        String urlPrefix = "http://ichart.finance.yahoo.com/x?s=";
        String urlSuffix = "&a=1&b=1&c=1900&d=1&e=1&f=2099&g=v&y=0&z=30000";
        String url = urlPrefix + ticker + urlSuffix;
        DownloadUtility.downloadFile(url, filePath);
    }
}
