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
 * https://www.kuaidaili.com/free/inha/1/
 **/
@Slf4j
public class IPHaiCrawlerJob extends AbstractCrawler {
    public IPHaiCrawlerJob(ConcurrentLinkedQueue<ProxyIp> proxyIpQueue, String pageUrl) {
        super(proxyIpQueue, pageUrl);
    }

    public IPHaiCrawlerJob(ConcurrentLinkedQueue<ProxyIp> proxyIpQueue, String pageUrl, int pageCount) {
        super(proxyIpQueue, pageUrl, pageCount);
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

                proxyIp.setIp(element.child(0).text());
                proxyIp.setPort(Integer.parseInt(element.child(1).text()));
                proxyIp.setLocation(element.child(5).text());
                proxyIp.setType(element.child(3).text());
                proxyIp.setAvailable(true);
                proxyIp.setCreateTime(new Date());
                proxyIp.setLastValidateTime(new Date());
                proxyIp.setValidateCount(0);
                proxyIpQueue.offer(proxyIp);
            } catch (Exception e) {
                log.error("kuaidailiCrawlerJob error:{0}",e);
            }
        }
    }

    public static void main(String[] args) {
        ConcurrentLinkedQueue<ProxyIp> proxyIpQueue = new ConcurrentLinkedQueue<>();

        IPHaiCrawlerJob ipHaiCrawlerJob = new IPHaiCrawlerJob(proxyIpQueue, "http://www.iphai.com/");

        ipHaiCrawlerJob.run();
    }


}