package com.increff.pos.service;
import com.increff.pos.model.OrderItemDataList;
import com.increff.pos.pojo.OrderItemPojo;
import org.apache.fop.apps.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class InvoiceService {
    @Autowired
    private OrderService oService;
    @Autowired
    private OrderItemService orderItemService;
    private final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
    @Transactional(rollbackOn = ApiException.class)
    public HttpServletResponse getOrderInvoice(int orderId) throws ApiException, IOException, TransformerException {
        List<OrderItemPojo> o = orderItemService.getOrder(orderId);
        ZonedDateTime time = oService.get(orderId).getTime();
        Double total = 0.;

        for (OrderItemPojo i : o) {
            total += i.getQuantity() * i.getSellingPrice();
        }
        OrderItemDataList oItem = new OrderItemDataList(o, time, total, orderId);
        String invoice="main/resources/Invoice/invoice"+orderId+".pdf";
        String xml = jaxbObjectToXML(oItem);
        File xsltFile = new File("src", "main/resources/com/increff/pos/invoice.xml");
        File pdfFile = new File("src", invoice);
        convertToPDF(oItem, xsltFile, pdfFile, xml);
        HttpServletResponse response = null;
        File file=new File("src/main/resources/Invoice/invoice"+orderId+".pdf");
        if (file.exists()) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
            response.setContentLength((int) file.length());
            InputStream inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
        return response;
    }

    private static String jaxbObjectToXML(OrderItemDataList orderItemList) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(OrderItemDataList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(orderItemList, sw);
            String xmlContent = sw.toString();
            return xmlContent;

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return "";
    }
    private void convertToPDF(OrderItemDataList team, File xslt, File pdf, String xml)
            throws IOException, TransformerException {

        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        OutputStream out = new java.io.FileOutputStream(pdf);
        out = new java.io.BufferedOutputStream(out);
        try {
            Fop fop = null;
            try {
                fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            } catch (FOPException e) {
                throw new RuntimeException(e);
            }
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslt));
            Source src = new StreamSource(new StringReader(xml));
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
        } catch (FOPException e) {
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
    }
}