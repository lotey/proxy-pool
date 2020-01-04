package com.chenerzhu.crawler.proxy.pool.job.crawler;

import com.chenerzhu.crawler.proxy.pool.entity.ProxyIp;
import com.chenerzhu.crawler.proxy.pool.entity.WebPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author vincent
 * @create 2019-11-11
 * http://www.goubanjia.com/
 **/
@Slf4j
public class QuanWangCrawlerJob extends AbstractCrawler {
    public QuanWangCrawlerJob(ConcurrentLinkedQueue<ProxyIp> proxyIpQueue, String pageUrl) {
        super(proxyIpQueue, pageUrl);
    }

    @Override
    public void parsePage(WebPage webPage) {
        Elements elements = webPage.getDocument().getElementsByTag("tr");
        Element element;
        ProxyIp proxyIp;
        for (int i = 1; i < elements.size(); i++) {
            try {
                element = elements.get(i);
                proxyIp = new ProxyIp();

                // Remove duplicate number in <p></p>
                Element childElement = element.child(0);
                childElement.select("p").remove();
                String ipAndPort = childElement.text().replaceAll("\\s+", "").replaceAll("\n", "");

                proxyIp.setIp(ipAndPort.split(":")[0]);
                proxyIp.setPort(Integer.parseInt(ipAndPort.split(":")[1]));

                proxyIp.setLocation(element.child(3).text());
                proxyIp.setType(element.child(2).text());
                proxyIp.setAvailable(true);
                proxyIp.setCreateTime(new Date());
                proxyIp.setLastValidateTime(new Date());
                proxyIp.setValidateCount(0);
                proxyIpQueue.offer(proxyIp);
            } catch (Exception e) {
                log.error("quanwangCrawlerJob error:{0}",e);
            }
        }
    }

    public static void main(String[] args) {
        ConcurrentLinkedQueue<ProxyIp> proxyIpQueue = new ConcurrentLinkedQueue<>();

        QuanWangCrawlerJob quanWangCrawlerJob = new QuanWangCrawlerJob(proxyIpQueue, "http://www.goubanjia.com/");

        quanWangCrawlerJob.run();
    }


}