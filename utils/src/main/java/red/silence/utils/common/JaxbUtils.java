package red.silence.utils.common;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

/**
 * 金三交互报文转换工具类
 * <p>使用时请在 {@} 类XmlSeeAlso注解中添加相应的类 </p>
 * <p>使用时copy  package-info.java至待解析类包下</p>
 * 
 * SCHEMA_LOCATION
 * TAXML_QNAME
 * getContext(JaxbUtils.class);
 * 修改配置
 * 
 * @author quiet
 * @date 2018年1月21日
 */
public class JaxbUtils {
	
	//schema
	private static final String SCHEMA_LOCATION = "";
	//
	private static final QName TAXML_QNAME = new QName(SCHEMA_LOCATION, "");
	
	private static Map<String, JAXBContext> contexts = new HashMap<String, JAXBContext>();
	
	private static JAXBContext getDefaultContext() throws JAXBException {
		//JaxbUtils 修改
		return getContext(JaxbUtils.class);
	}
	
	private static Marshaller getDefaultMarshaller() throws JAXBException {
		Marshaller marshaller = getDefaultContext().createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, SCHEMA_LOCATION);
		
		return marshaller;
	}
	
	private static JAXBElement<JaxbUtils> getDefaultJaxbElement(JaxbUtils jaxbUtils) {
		return new JAXBElement<JaxbUtils>(TAXML_QNAME, JaxbUtils.class, null, jaxbUtils);
	}
	
	private static JAXBContext getContext(Class<?> T) throws JAXBException {
        JAXBContext jaxbContext = contexts.get(T.getName()); 
        if(null == jaxbContext) {
            jaxbContext = JAXBContext.newInstance(T);
            contexts.put(T.getName(), jaxbContext);
        }
        return jaxbContext;
    }
    
    private static Marshaller getMarshaller(Class<?> T) throws JAXBException {
        Marshaller marshaller = getContext(T).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, SCHEMA_LOCATION);
        
        return marshaller;
    }
    
    @SuppressWarnings("unchecked")
    private static <T> JAXBElement<T> getJaxbElement(T t, QName qName) {
        return new JAXBElement<T>(qName, (Class<T>) t.getClass(), null, t);
    }
	
    /**
     * 转换对象为xml
     * @param 
     * @return xml
     * @throws JAXBException
     */
	public static String convertToXML(JaxbUtils jaxbUtils) throws JAXBException {
	    StringWriter sw = new StringWriter();
		getDefaultMarshaller().marshal(getDefaultJaxbElement(jaxbUtils), sw);
		return sw.toString();
	}
	
	/**
	 * xmlToObject
	 * @param t 待转换对象
	 * @param qName 命名空间，可为null
	 * @return
	 * @throws JAXBException
	 */
	public static <T> String convertToXML(T t, QName qName) throws JAXBException {
        StringWriter sw = new StringWriter();
        if(null != qName) {
        	getMarshaller(t.getClass()).marshal(getJaxbElement(t, qName), sw);
        } else {
        	getMarshaller(t.getClass()).marshal(t, sw);
        }
        
        return sw.toString();
    }
	
	/**
	 * xmlToObject
	 * @param xml
	 * @return Object
	 * @throws JAXBException
	 */
	public static <T extends JaxbUtils> T convertToTaxDoc(String xml) throws JAXBException {
		return convertToTaxDoc(xml, JaxbUtils.class);
	}
	
	/**
	 * xmlToObject
	 * @param xml
	 * @param T
	 * @return 
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
    public static <T> T convertToTaxDoc(String xml, Class<? extends JaxbUtils> T) throws JAXBException {
	    StringReader reader = new StringReader(xml);
	    Unmarshaller unmarshaller = getDefaultContext().createUnmarshaller();
	    JAXBElement<T> result= (JAXBElement<T>)unmarshaller.unmarshal(reader);
        return (T)result.getValue();
	}
	
	/**
	 * xmlToObject
	 * @param xml
	 * @param T
	 * @return
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
    public static <T> T convertToObject(String xml, Class<?> T) throws JAXBException {
	    StringReader reader = new StringReader(xml);
        Unmarshaller unmarshaller = getContext(T).createUnmarshaller();
        JAXBElement<T> result= (JAXBElement<T>)unmarshaller.unmarshal(reader);
        return (T)result.getValue();
	}
}
