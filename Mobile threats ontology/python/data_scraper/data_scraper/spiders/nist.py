import scrapy
from scrapy import cmdline


class NistSpider(scrapy.Spider):
    name = 'nist'
    allowed_domains = ['pages.nist.gov']
    start_urls = ['https://pages.nist.gov/mobile-threat-catalogue/categories.html']

    def parse(self, response):
        for items in response.xpath(".//div[@class='content container']/ul/li"):
            link = items.xpath(".//a/@href").extract_first()
            name = items.xpath(".//a/text()").extract_first().replace(':', '').replace(' ', '_').strip()
            yield scrapy.Request(response.urljoin(link), callback=self.parse_categories, cb_kwargs={'name': name})

    def parse_categories(self, response, name):
        for items in response.xpath(".//ul[@class='threat-list']/li"):
            link = items.xpath(".//a/@href").extract_first()
            yield scrapy.Request(response.urljoin(link), callback=self.parse_data, cb_kwargs={'name': name})

    def parse_data(self, response, name):
        id = response.xpath(".//div[@class='content container']/p[3]/text()").extract_first()
        category = response.xpath(".//div[@class='content container']/p[2]/text()").extract_first()
        description = response.xpath(".//div[@class='content container']/p[4]/text()").extract_first()
        countermeasure = response.xpath(".//div[@class='content container']/div[@class='paragraph'][4]").extract_first()
        yield {
            name: {
                'id': id,
                'name': name,
                'category': category,
                'description': description,
                'countermeasure': countermeasure
            }
        }


cmdline.execute("scrapy crawl nist -o test.xml".split())
