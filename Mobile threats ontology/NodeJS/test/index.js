// For http requests
const axios = require('axios');
// For parsing HTML
const cheerio = require('cheerio');
// For writing file
const fs = require('fs-extra');
// For beautifying XML, indentation and spaces
var format = require('xml-formatter');

const PREFIX = 'https://pages.nist.gov';

// Get the website as HTML
async function getHtml(url) {
    const { data: html } = await axios.get(url);
    return html;
}

async function init() {
    const url = "https://pages.nist.gov/mobile-threat-catalogue/application.html";
    
    const html = await getHtml(url);
    const xml = await getSiteAsXML(html);
    
    // Write to file
    await fs.writeFile('output.xml', xml);
    console.log('Done');
}

async function getSiteAsXML(html) {
    const $ = cheerio.load(html);
    // Find the products list elements
    const page = $('#page');
    // Find the li elements inside threat-list ul
    const list = page.find('.threat-list li');
    let new_list = ["",""];
    // The first 12 elements (APP-0 till APP-11) should be in first ul
    for(var i = 0; i < 12; i++) {
        let _a = $(list[i]).find('a')[0];
        let a = $(_a).attr('href');
        new_list[0] += await getSubItem(a);
    }
    // The rest of li elements (APP-12 till APP-43) should be in second ul
    for(var i = 12; i < list.length; i++) {
        let _a = $(list[i]).find('a')[0];
        let a = $(_a).attr('href');
        new_list[1] += await getSubItem(a);
    }

    let ul = page.find('.threat-list');
    // Delete the existing `li` with `a` tags from first ul
    $(ul[0]).empty();
    // Add the newly created APP-0 - APP-11 page to the first ul list
    $(ul[0]).html(new_list[0]);
    // Delete the existing `li` with `a` tags from second ul
    $(ul[1]).empty();
    // Add the newly created APP-11 - APP-43 page to the second ul list
    $(ul[1]).html(new_list[1]);

    // Change href="/..." to href="https://pages.nist.gov/..."
    fixAnchors($, page);

    /*
     * Since we are creating a new XML file, the
     * nbsp is a character used in the website, it is undefined in XML, therefore we define it in header
     * the XML version and encoding is optional, but a best practice 
     */
    const header = `<?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE doc [
      <!ENTITY nbsp "&#160;">
    ]>\n`;

    // Wrap the changes inside a single tag `doc`, can be anything.
    const content = '<doc>' + page.html() + '</doc>';

    // Remove unnessary spaces and correct the indentation
    var formattedXml = header + format(content);
    return formattedXml;

}

async function getSubItem(url) {
    let full_url = PREFIX + url;
    console.log('Working on ' + full_url);
    const html = await getHtml(full_url);
    const $ = cheerio.load(html);
    // Find the page id DIV
    const page = $('#page');
    
    page.find('#issue-list').remove();
    page.find('#github-issues').remove();
    page.find('script').remove();

    // Change href="/..." to href="https://pages.nist.gov/..."
    fixAnchors($, page);

    // Wrap the contents inside single li tag
    const content = '<li>' + page.html() + '</li>';
    return content;

}

function fixAnchors($, page) {
    let anchors = page.find('a');
    for(var i = 0; i < anchors.length; i++) {
        let x = $(anchors[0]);
        let href = x.attr('href');
        if(href.startsWith('/'))
            x.attr('href', PREFIX + href);
    }
}

// Run the main function
init();