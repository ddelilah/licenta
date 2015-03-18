package parsers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author oneadmin
 */
public  class OpennebulaUtils {

        private static Pattern cpuPattern = Pattern.compile("CPU\\s*=.*");
        private static Pattern memPattern = Pattern.compile("MEMORY\\s*=.*");
        private static Pattern netPatern = Pattern.compile("NIC.*\\[.*\\]");
        private static Pattern vncPortPatern = Pattern.compile("PORT\\s*=.*");
        private static Pattern bridgePattern = Pattern.compile("BRIDGE\\s*=.*");
        private static Pattern namePattern = Pattern.compile("NAME\\s*=.*");

        //TODO: de setat si VNC password in case i need it
        public static String configureVMTemplate(String source, String vmName, Double cpuValue,
                Integer memoryValue, String attachedNetworkID, String requestedIP, Integer vncPort, String vncPassword) {
            String result = source;

            Matcher nameMatcher = namePattern.matcher(result);
            if (nameMatcher.find()) {
                result = result.replace(result.substring(nameMatcher.start(), nameMatcher.end()), "NAME = " + vmName);
            }

            Matcher cpuMatcher = cpuPattern.matcher(result);
            if (cpuMatcher.find()) {
                result = result.replace(result.substring(cpuMatcher.start(), cpuMatcher.end()), "CPU = " + cpuValue);
            }

            Matcher memMatcher = memPattern.matcher(result);
            if (memMatcher.find()) {
                result = result.replace(result.substring(memMatcher.start(), memMatcher.end()), "MEMORY = " + memoryValue);

            }

            Matcher nicMatcher = netPatern.matcher(result);
            if (nicMatcher.find()) {
                result = result.replace(result.substring(nicMatcher.start(), nicMatcher.end()),
                        " NIC = [ NETWORK_ID = \"" + attachedNetworkID
                        + "\" , IP=\" " + requestedIP + "\" ] ");
            }

            Matcher vncPortMatcher = vncPortPatern.matcher(result);
            if (vncPortMatcher.find()) {
                result = result.replace(result.substring(vncPortMatcher.start(), vncPortMatcher.end()), " PORT = \"" + vncPort + "\", ");
            }

            return result;
        }

        public static String formatXML(String unformattedXml) {
            try {
                final Document document = parseXmlFile(unformattedXml);

                OutputFormat format = new OutputFormat(document);
                format.setLineWidth(65);
                format.setIndenting(true);
                format.setIndent(2);
                Writer out = new StringWriter();
                XMLSerializer serializer = new XMLSerializer(out, format);
                serializer.serialize(document);

                return out.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static Document parseXmlFile(String in) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(in));
                return db.parse(is);
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

   



