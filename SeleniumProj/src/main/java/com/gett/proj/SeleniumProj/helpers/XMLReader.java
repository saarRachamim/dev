package com.gett.proj.SeleniumProj.helpers;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class XMLReader {
	private File xmlFile;
	private int numberOfIteraraions;
	private String[] urls;
	private String[] names;
	
	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public int getNumberOfIteraraions() {
		return numberOfIteraraions;
	}

	public void setNumberOfIteraraions(int numberOfIteraraions) {
		this.numberOfIteraraions = numberOfIteraraions;
	}

	public String[] getUrls() {
		return urls;
	}

	public void setUrls(String[] urls) {
		this.urls = urls;
	}

	public XMLReader(String relativeFilePath, String fileName) {
		this.xmlFile = new File(relativeFilePath, fileName);
	}

	public void readFromXMLFile() {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("test");

			setNumberOfIteraraions(nList.getLength());
			String[] urls = new String[getNumberOfIteraraions()];
			String[] names = new String[getNumberOfIteraraions()];
			for (int i = 0; i < getNumberOfIteraraions(); i++) {
				Node nNode = nList.item(i);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					urls[i] = eElement.getElementsByTagName("url").item(0).getTextContent();
					names[i] = eElement.getElementsByTagName("name").item(0).getTextContent();
				}
			}
			setUrls(urls);
			setNames(names);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}