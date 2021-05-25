
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WomanFashion {

    private static String IMAGE_DESTINATION_FOLDER = "E://webscrapping//src//main//Images";

    private static String detailsFile = "E://webscrapping//src//main//Images//topsDetails.csv";
    private static String skuFile = "E://webscrapping//src//main//Images//SKU.csv";

    public static void main(String[] args) throws IOException {



        BufferedWriter out = new BufferedWriter(
                new FileWriter(detailsFile, true));

        BufferedWriter outSKU = new BufferedWriter(
                new FileWriter(skuFile, true));

        out.flush();
        outSKU.flush();


        List<String> skuList = new ArrayList<>();

        out.write("BrandName,BrandDetails,Price");
        out.newLine();

        //connect to the website and get the document
        for(int i=1;i<50;i++) {

            String strURL = "https://www.zalando.co.uk/womens-clothing-tops/?p="+i;
            Document document = Jsoup
                    .connect(strURL)
                    .userAgent("Mozilla/5.0")
                    .timeout(200 * 1000)
                    .get();

            //select all img tags
            Elements imageElements = document.select("img");

            Elements reqDivs = document.select("div.qMZa55.SQGpu8.iOzucJ.JT3_zV.DvypSJ");

            for (Element element : reqDivs)
            {

                String href = extractBrandPageLink(element);

                String sku = extractSKU(href);
                if (sku != null && sku != "") {
                    outSKU.write(sku);
                    outSKU.newLine();
                }

//                Document documentEle = Jsoup
//                        .connect(href)
//                        .userAgent("Mozilla/5.0")
//                        .timeout(200 * 1000)
//                        .get();

//                String brandFolder = IMAGE_DESTINATION_FOLDER + getBrand(documentEle);
//
//                String productDetails = getCSVProductDetail(documentEle);
//                out.write(productDetails);
//                out.newLine();
//
//                Elements brandElementImages = documentEle.select("div.adFHlH._0xLoFW._7ckuOK.mROyo1").select("div.KVKCn3.KLaowZ.mo6ZnF").select("img");

//                int count = 0;
//                for (Element image : brandElementImages)
//                {
//
//                    String strImageURL = image.attr("abs:src");
//
//                    //download image one by one
//                    downloadImage(strImageURL, brandFolder);
//                    count++;
//                    if (count == 5)
//                    {
//                        break;
//                    }
//
//                }

            }


        }
        outSKU.close();

//        out.close();

    }

    private static String extractSKU(String href) {

        String imagename = href.substring(0, href.lastIndexOf("."));
        imagename = imagename.substring(imagename.lastIndexOf("/"));

        String[] skuList = imagename.split("-");

        if(skuList.length < 2) {
            return null;
        }

        String sku = skuList[skuList.length-2] + "-" + skuList[skuList.length-1];

        return sku.toUpperCase();

    }

    public static String getCSVProductDetail(Document document) {
        String brandName = document.select(".uc9Eq5._5Yd-hZ").text();
        String brandDetail = document.select("._1PY7tW._9YcI4f").text();
        String price = document.select(".uqkIZw").text();

        return brandName + "," + brandDetail + "," + price;
    }

    private static String getBrand(Document document) {
        return document.select(".uc9Eq5._5Yd-hZ").text().replace(" ", "");
    }

    public static String extractBrandPageLink(Element element) {
        String uri = element.select(".VfpFfd.g88eG_.oHRBzn.LyRfpJ._LM.JT3_zV.g88eG_").attr("href");
        String url = "https://www.zalando.co.uk";

        return (url+uri);
    }

    private static void downloadImage(String strImageURL, String folderPath){


        try {

            File f1 = new File(folderPath);

            f1.mkdirs();

            //get file name from image path
            String strImageName =
                    strImageURL.substring( strImageURL.lastIndexOf("/") + 1 );

            if(strImageName.lastIndexOf("?") != -1) {
                strImageName= strImageName.substring(0, strImageName.lastIndexOf("?"));
            }
            if(strImageName.contains("&")) {
                strImageName= strImageName.substring(0, strImageName.indexOf("&"));
            }

            //open the stream from URL
            URL urlImage = new URL(strImageURL);
            InputStream in = urlImage.openStream();

            byte[] buffer = new byte[4096];
            int n = -1;

            OutputStream os =
                    new FileOutputStream( folderPath + "/" + strImageName );

            //write bytes to the output stream
            while ( (n = in.read(buffer)) != -1 ){
                os.write(buffer, 0, n);
            }

            //close the stream
            os.close();

//            System.out.println("Image saved");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
