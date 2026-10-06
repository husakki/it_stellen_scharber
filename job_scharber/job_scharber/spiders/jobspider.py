import scrapy


class JobSpider(scrapy.Spider):
    name = "jobspider"
    allowed_domains = ["germantechjobs.de"]
    start_urls = ["https://germantechjobs.de"]

    def parse(self, response):
        pass
