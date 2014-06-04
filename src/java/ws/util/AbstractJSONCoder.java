/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.util;

import com.fasterxml.jackson.jr.ob.JSON;
import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author masaru
 */
//public abstract class JSONCoder<T> implements Encoder.Text<T>, Decoder.Text<T> {
public abstract class AbstractJSONCoder<T> implements Encoder.Text<T>, Decoder.Text<T> {

		protected Class<T> type;

		@Override
		public void init(EndpointConfig endpointConfig) {

				ParameterizedType $thisClass = (ParameterizedType) this.getClass().getGenericSuperclass();
				Type $T = $thisClass.getActualTypeArguments()[0];
				if ($T instanceof Class) {
						type = (Class<T>) $T;
				} else if ($T instanceof ParameterizedType) {
						type = (Class<T>) ((ParameterizedType) $T).getRawType();
				}
		}

		private static final Logger logger = Logger.getLogger("JSONCoder");

		@Override
		public String encode(T pojo) throws EncodeException {
				logger.log(Level.INFO, new StringBuilder()
						.append(type)
						.append("| [coder] encoding..")
						.append(pojo)
						.toString());
				String json = null;
				try {
//						ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
						json = JSON.std.asString(pojo);
						//json = ow.writeValueAsString(pojo);
				} catch (IOException e) {
						logger.log(Level.SEVERE, e.toString());
						throw new EncodeException(json, e.getMessage());
				}
//				logger.log(Level.INFO, new StringBuilder()
//						.append("[coder] done: ")
//						.append(json)
//						.toString());
				return json;
		}

		@Override
		public T decode(String json) throws DecodeException {
//				logger.log(Level.INFO, new StringBuilder()
//						.append("[coder] decoding.. ")
//						.append(json)
//						.toString());
				try {
						T message = JSON.std.beanFrom(type, json);
//						logger.log(Level.INFO, new StringBuilder()
//								.append("[coder] done ")
//								.append(message)
//								.toString());
						return message;
				} catch (IOException e) {
						logger.log(Level.SEVERE, e.toString());
						throw new DecodeException(json, e.getMessage());
				}
		}

		@Override
		public boolean willDecode(String json) {
//				logger.log(Level.INFO, new StringBuilder()
//						.append("[coder] will decoding..")
//						.toString());
				try {
						JSON.std.beanFrom(type, json);
//						logger.log(Level.INFO, new StringBuilder()
//								.append("[coder] OK.. return true")
//								.toString());
						return true;
				} catch (IOException e) {
						logger.log(Level.SEVERE, new StringBuilder()
								.append("[coder] NG.. return false")
								.toString());
						logger.log(Level.SEVERE, e.toString());
						return false;
				}

		}

//		@Override
//		public void init(EndpointConfig config) {
//				logger.log(Level.INFO, "[coder] init");
//		}
		@Override
		public void destroy() {
//				logger.log(Level.INFO, "[coder] destroy");
		}
}
