package org.microapp.membernet.vo;

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

public class MembershipVOsReader implements MessageBodyReader<MembershipVOs> {

	public boolean isReadable(Class<?> type, Type genericType, Annotation[] arg2,
			MediaType arg3) {
		return type == MembershipVOs.class;
	}

	public MembershipVOs readFrom(Class<MembershipVOs> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> arg4, InputStream entityStream)
			throws IOException, WebApplicationException {
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(MembershipVOs.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
			unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);
			MembershipVOs mvos = unmarshaller.unmarshal(new StreamSource(entityStream), MembershipVOs.class).getValue();
			return mvos;
		} catch(JAXBException ex) {
			throw new ProcessingException("Error while deserializing MembershipVOs bean", ex);
		}
		
	}

}
