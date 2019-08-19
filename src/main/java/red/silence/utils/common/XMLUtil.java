package red.silence.utils.common;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;

/**
 * 描述：封装了XML转换成Object，Object转换成XML
 * 创建时间：2017年9月27日 21:58:05
 * 创建人：王旭
 */
public class XMLUtil {
	
	public static Map<String, JAXBContext> contextMap = new HashMap<String, JAXBContext>();
	
	public static JAXBContext newInstance(Class clazz){
		
		if(contextMap.get(clazz.getName()) !=null){
			return contextMap.get(clazz.getName());
		}else{			
			JAXBContext context = null;
			try {
				context = JAXBContext.newInstance(clazz);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			contextMap.put(clazz.getName(), context);
			return context;
		}
	}

	/**
	 * 描述：将Object转换为XML
	 * @param obj
	 * @return
	 */
	public static String convertToXML(Object obj){
		
		//创建输出流
		StringWriter stringWriter = new StringWriter();
		
		try {
		
			// 利用JAXB-API-2.2.11.jar自带的转换类实现
			JAXBContext context = newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller(); 
			
			// 格式化XML输出的格式  
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,  
                    Boolean.TRUE);  
            // 设置字符集
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            
            // 将对象转换成输出流形式的XML  
            marshaller.marshal(obj, stringWriter);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stringWriter.toString();
	}
	
	/**
	 * 描述：根据编码格式将Object转换为XML
	 * @param obj
	 * @return
	 */
	public static String convertToXMLCode(Object obj, String code){
		
		//创建输出流
		StringWriter stringWriter = new StringWriter();
		
		try {
		
			// 利用JAXB-API-2.2.11.jar自带的转换类实现
			JAXBContext context = newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller(); 
			
			// 格式化XML输出的格式  
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,  
                    Boolean.TRUE);  
            // 设置字符集
            marshaller.setProperty(Marshaller.JAXB_ENCODING, code);
            
            // 将对象转换成输出流形式的XML  
            marshaller.marshal(obj, stringWriter);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stringWriter.toString();
	}
	
	/**
	 * 描述：将Object根据路径转换成XML文件
	 * 创建人：王旭
	 * 创建时间：2017年9月27日 22:15:21
	 * @param obj 对象
	 * @param path 路径
	 */
	public static void convertToXml(Object obj, String path) {  
        try {  
        	
            // 利用JAXB-API-2.2.11.jar自带的转换类实现  
            JAXBContext context = newInstance(obj.getClass());  
            Marshaller marshaller = context.createMarshaller();  
            
            // 格式化XML输出的格式  
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,  
                    Boolean.TRUE);  
            // 设置字符集
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            
            // 将对象转换成输出流形式的XML  
            // 创建输出流  
            FileWriter fileWriter = null;  
            
            try {  
            	fileWriter = new FileWriter(path);  
            } catch (IOException e) {  
                e.printStackTrace();  
            }
            
            marshaller.marshal(obj, fileWriter);  
        } catch (JAXBException e) {  
            e.printStackTrace();  
        }  
    }  
	
	/**
	 * 描述：将String类型的XML转换成对象
	 * 创建人：王旭
	 * 创建时间：2017年9月27日 22:20:17
	 * @param clazz 转换类型的Object.class
	 * @param xmlStr String类型的XML
	 * @return Object
	 */
	@SuppressWarnings("rawtypes")
	public static Object convertXmlStrToObject(Class clazz, String xmlStr) {  
        Object xmlObject = null;  
        try {  
            JAXBContext context = newInstance(clazz);  
            // 进行将XML转成对象的核心接口  
            Unmarshaller unmarshaller = context.createUnmarshaller();  
            StringReader sr = new StringReader(xmlStr);  
            
            xmlObject = unmarshaller.unmarshal(sr);  
        } catch (JAXBException e) {
            RuntimeException rte = new RuntimeException("解析期初报文发生异常");
            rte.addSuppressed(e);
            
            throw rte;
        }
        return xmlObject;  
    }
	
	/**
	 * 方法描述：将xml转成java对象 
	 * @param t 类类型
	 * @param xmlStr xml字符串
	 * @param xpath  xpath表达式
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertXmlStrToObject(Class<T> t, String xml, String xpath) {  
        T xmlObject = null;  
        try {  
            JAXBContext context = newInstance(t);  
            // 进行将XML转成对象的核心接口  
            Unmarshaller unmarshaller = context.createUnmarshaller(); 
            String root = getElementXML(xml, xpath);
            if(StringUtils.isNotEmpty(root)){
                StringReader sr = new StringReader(root);  
                xmlObject = (T) unmarshaller.unmarshal(sr);  
            }
        } catch (JAXBException e) {  
            e.printStackTrace();  
        }  
        return xmlObject;  
    }
	
	/**
	 * 方法描述：把xml转成java对象数组（List）
	 * @param t  类类型
	 * @param xml 完成xml
	 * @param xpath  xpath表达式
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> convertXmlStrToList(Class<T> t, String xml, String xpath) {  
         
        List<Element> elements = getElements(xml, xpath);
        List<T> list = new ArrayList<T>();
        if(elements !=null && elements.size()>0){
        	for(Element element : elements){
        		String elXml = getElementXML(element);
        		T obj = (T)convertXmlStrToObject(t, elXml);
        		list.add(obj);
        	}
        }
        
        return list;  
    }
	
	/**
	 * 描述：将FILE类型的XML转换成对象 
	 * @param clazz 转换类型的Object.class
	 * @param xmlPath XML文件的路径
	 * @return Object
	 */
    @SuppressWarnings("rawtypes")
	public static Object convertXmlFileToObject(Class clazz, String xmlPath) {  
        Object xmlObject = null;  
        try {  
            JAXBContext context = newInstance(clazz);  
            Unmarshaller unmarshaller = context.createUnmarshaller();  
            FileReader fr = null;  
            try {  
                fr = new FileReader(xmlPath);  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            }  
            xmlObject = unmarshaller.unmarshal(fr);  
        } catch (JAXBException e) {  
            e.printStackTrace();  
        }  
        return xmlObject;  
    }  
    
    /**
     * 方法描述：获取XML结点值，
     * @param xml xml字符串
     * @param path	xpath路径表达式
     * @return
     */
	public static String getElementValue(String xml, String path){
		
		String value = null;
		Element element = getElement(xml, path);
		if(element !=null){
			value = element.getText();			
		}		
		
		return value;
	}
	
	/**
	 * 方法描述：获取XML结点字符串,注:不带命名空间
	 * @param xml	xml字符串
	 * @param path  路径表达 式
	 * @return
	 */
	public static String getElementXML(String xml, String path){
		
		String value = null;
		
		Element element = getElement(xml, path);
		
		return getElementXML(element);
	}
	
	/**
	 * 修改内容：不删除namespace--避免因非隐藏型namespace属性导致的xml解析失败问题
	 * 修改人：WangDongling
	 * 修改时间：2019年2月20日16:17:18
	 * @param element
	 * @return
	 */
	public static String getElementXML(Element element){
		
		String value = null;
		
		if(element !=null){
			value = element.asXML();
			
			/*
			 * 不删除namespace--避免因非隐藏型namespace属性导致的xml解析失败问题
			 * String namespace = element.getNamespaceURI();
			if(namespace !=null && !"".equals(namespace)){
				value = value.replaceAll("xmlns=\""+namespace+"\"", "");
			}*/
		}		
		return value;
	}
	
	/**
	 * 方法描述：获取xml中单个节点
	 * @param xml
	 * @param path
	 * @return
	 */
	public static Element getElement(String xml, String path){
		
		Element element = null;
		try {
			Document doc = DocumentHelper.parseText(xml);			
			//处理命名空间，防止因根节点带命名空间，导致无法取值问题
			String namespace = doc.getRootElement().getNamespaceURI();
			if(namespace !=null && !"".equals(namespace)){
				Map<String, String> map = new HashMap<String, String>();
				map.put("xmlns", namespace);
				XPath xpath = doc.createXPath(path);
				xpath.setNamespaceURIs(map);
				element = (Element)xpath.selectSingleNode(doc);
			}else{
				element = (Element)doc.selectSingleNode(path);
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return element;
	}
	
	/**
	 * 方法描述：获取xml中单个节点
	 * @param xml
	 * @param path
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> getElements(String xml, String path){
		
		List<Element> elements = null;
		try {
			Document doc = DocumentHelper.parseText(xml);			
			//处理命名空间，防止因根节点带命名空间，导致无法取值问题
			String namespace = doc.getRootElement().getNamespaceURI();
			if(namespace !=null && !"".equals(namespace)){
				Map<String, String> map = new HashMap<String, String>();
				map.put("xmlns", namespace);
				XPath xpath = doc.createXPath(path);
				xpath.setNamespaceURIs(map);
				elements = (List<Element>)xpath.selectNodes(doc);
			}else{
				elements = (List<Element>)doc.selectNodes(path);
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return elements;
	}
}
