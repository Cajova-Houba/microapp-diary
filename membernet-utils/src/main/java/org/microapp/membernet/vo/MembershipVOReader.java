package org.microapp.membernet.vo;

import org.microapp.membernet.vo.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.UnmarshallerProperties;


public class MembershipVOReader implements MessageBodyReader<MembershipVO> {

	public boolean isReadable(Class<?> type, Type genericType, Annotation[] arg2,
			MediaType arg3) {
		return type == MembershipVO.class;
	}

	public MembershipVO readFrom(Class<MembershipVO> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> arg4, InputStream entityStream)
			throws IOException, WebApplicationException {
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(MembershipVO.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
			unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);
			MembershipVO mvo = unmarshaller.unmarshal(new StreamSource(entityStream), MembershipVO.class).getValue();
			return mvo;
		} catch(JAXBException ex) {
			throw new ProcessingException("Error while deserializing MembershipVO bean", ex);
		}
		
	}

}
