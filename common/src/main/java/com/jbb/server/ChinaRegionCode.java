package com.jbb.server;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author VincentTang
 * @date 2018年1月30日
 */
public class ChinaRegionCode {

    public static String SITE_URL = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/index.html";
    private static List<RegionEntry> regions = new ArrayList<RegionEntry>();

    public static void main(String[] args) {
        System.out.println("抓取开始:" + new Date());
        getProvince();
        StringBuffer content = new StringBuffer();
        for (RegionEntry one : regions) {
            content.append("insert into sys_region values('").append(one.getCode()).append("', '").append(one.getCode())
                .append("',null, null,'").append(one.getCode()).append("', '000000', '").append(one.getName())
                .append("', '1' );\r\n");
            for (RegionEntry two : one.getSub()) {
                content.append("insert into sys_region values('").append(two.getCode()).append("', '")
                    .append(two.getCode()).append("',null, '").append(two.getCode()).append("','").append(one.getCode())
                    .append("', '").append(one.getCode()).append("', '").append(two.getName()).append("', '0' );\r\n");
                for (RegionEntry three : two.getSub()) {
                    content.append("insert into sys_region values('").append(three.getCode()).append("', '")
                        .append(three.getCode()).append("', '").append(three.getCode()).append("', '")
                        .append(two.getCode()).append("','").append(one.getCode()).append("', '").append(two.getCode())
                        .append("', '").append(three.getName()).append("', '0' );\r\n");
                }
            }
        }

        System.out.println("抓取完毕:" + content.toString());
        // Region.writeFile(content.toString());
        System.out.println("抓取完毕:" + new Date());
    }

    private static void getProvince() {
        try {
            Document doc = Jsoup.parse(new URL(SITE_URL).openStream(), "GBK", SITE_URL); // Jsoup.connect(SITE_URL).get();
            Elements links = doc.select("tr.provincetr").select("a");
            RegionEntry region = null;
            for (Element e : links) {
                region = new RegionEntry();
                String href = e.attr("href");
                String[] arr = href.split("\\.");
                String code = arr[0];
                if (arr[0].length() < 6) {
                    for (int i = 0; i < 6 - arr[0].length(); i++) {
                        code += "0";
                    }
                }

                region.setCode(code);
                region.setName(e.text());

                String absHref = e.attr("abs:href");
                // System.out.println("============" + region.getName() + "================");
                // System.out.println(region.getCode() + "\t" + region.getName());
                getCity(absHref, region);
                regions.add(region);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取市地址
     * 
     * @param url
     * @param region
     */
    private static void getCity(String url, RegionEntry region) {
        Document doc;
        try {
            doc = Jsoup.parse(new URL(url).openStream(), "GBK", url); // Jsoup.connect(url).get().charset(charset);
            Elements links = doc.select("tr.citytr");
            RegionEntry city;
            for (Element e : links) {
                city = new RegionEntry();
                Elements alist = e.select("a");
                Element codeE = alist.get(0);
                Element codeN = alist.get(1);
                String name = codeN.text();

                String code = codeE.text();
                code = code.substring(0, 6);

                if ("市辖区".equals(name)) {
                    name = region.getName();
                    // code = region.getCode();
                }

                city.setCode(code);
                city.setName(name);

                String absHref = codeE.attr("abs:href");
                //System.out.println("============" + city.getName() + "================");
//                 System.out.println(city.getCode() + "\t" + city.getName());
                System.out.println(city.getCode() + "\t" + region.getName() + "\t" + city.getName());
                getArea(absHref, city, region);
                if (region.getSub() == null) {
                    region.setSub(new ArrayList<RegionEntry>());
                }
                region.getSub().add(city);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取区县地址
     * 
     * @param url
     * @param region
     */
    private static void getArea(String url, RegionEntry region, RegionEntry city) {
        Document doc;
        try {
            doc = Jsoup.parse(new URL(url).openStream(), "GBK", url); // Jsoup.connect(url).get();
            Elements links = doc.select("tr.countytr");
            RegionEntry area;
            for (Element e : links) {
                area = new RegionEntry();
                Elements alist = e.select("a");
                if (alist.size() > 0) {
                    Element codeE = alist.get(0);

                    String code = codeE.text();
                    code = code.substring(0, 6);
                    area.setCode(code);

                    Element codeN = alist.get(1);
                    String name = codeN.text();
                    area.setName(name);
                    if (region.getSub() == null) {
                        region.setSub(new ArrayList<RegionEntry>());
                    }

                    System.out.println(
                        area.getCode() + "\t" + city.getName() + "\t" + region.getName() + "\t" + area.getName());

                    region.getSub().add(area);
                } else {
                    alist = e.select("td");
                    String code = alist.get(0).text();
                    code = code.substring(0, 6);
                    area.setCode(code);
                    area.setName(alist.get(1).text());
                    if (region.getSub() == null) {
                        region.setSub(new ArrayList<RegionEntry>());
                    }
                    System.out.println(
                        area.getCode() + "\t" + city.getName() + "\t" + region.getName() + "\t" + area.getName());
                    region.getSub().add(area);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}