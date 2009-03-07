/*
 * PlinthOS, Open Source Multi-Core and Distributed Computing.
 * Copyright 2003-2009, Emptoris Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.plinthos.core;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.plinthos.util.Constants;

 /**
  * <code>MessageFactory</code> holds all the Exception messages.
  * The message texts are kept in a Properties file and retrieved
  * using <code>java.util.ResourceBundle</code>.
  * Each message text is indexed with a "key".
  * <P>
  * <code>MessageFactory</code> formats the message text using a
  * <code>java.text.MessageFormat</code> object.
  * <P>
  *
  * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
  * @version $Revision: 1.2 $
  * @see $see also$
  */
 public class PlinthosMessageFactory {

     private MessageFormat message_Formatter;

     private ResourceBundle resource_Bundle;

	private Locale locale;

     private String resource_Name;

     /**
      * Creates PlinthosMessageFactory instance with default resourse bundle name
      * and US locale
      *
      */
     public PlinthosMessageFactory()
     {
         this(Constants.ERROR_BUNDLE, Locale.US);
     }

     /**
      *  Creates PlinthosMessageFactory instance with default resourse bundle name
      * and specified locale
      *
      * @param locale locale type
      *
      */
     public PlinthosMessageFactory(Locale locale)
     {
         this(Constants.ERROR_BUNDLE, locale);
     }

     /**
      * Creates PlinthosMessageFactory instance with specified resourse bundle name
      * and specified locale
      *
      *
      * @param resourceName resourse bundle name
      * @param locale locale name
      *
      */
     public PlinthosMessageFactory(String resourceName, Locale locale)
     {

         this.resource_Name = resourceName;
         this.locale = locale;
         message_Formatter = new MessageFormat("");
         resource_Bundle = ResourceBundle.getBundle(resourceName, locale);
     }

     /**
      * Gets the formatted message for specified error code with runtime parameters.
      *
      *
      * @param errorCode Error code
      * @param errorParams runtime parameters, these parameter values are inserted with
      * in the error message at appropriate places.
      *
      * @return String formatted error message
      *
      */
     public String getMessage(int errorCode, Object[] errorParams)
     {
         return this.formatMessage(errorCode, errorParams);
     }

     /**
      * Gets the formatted message for specified error code.
      *
      *
      * @param errorCode $description$
      *
      * @return String formatted error message
      *
      */
     public String getMessage(int errorCode)
     {
         return this.formatMessage(errorCode, null);
     }

     /**
      * Formatts the message for specified error code with runtime parameters.
      *
      *
      *
      *
      *  @param errorCode Error code
      *  @param errorParams runtime parameters, these parameter values are inserted with
      *  in the error message at appropriate places.
      *
      *
      * @return String formatted error message
      *
      */
     private String formatMessage(int errorCode, Object[] errorParams)
     {

         /* Now read the message from the resource bundle */
         String sErrorMessage = resource_Bundle.getString(String.valueOf(errorCode));
         if(errorParams != null)
         {
             return MessageFormat.format(sErrorMessage, errorParams);
         }
         else
         {
             return sErrorMessage;
         }
     }

     /**
      * Sets the local and re-load the resource bundle
      *
      *
      * @param locale local
      *
      */
     public void setLocale(Locale locale)
     {
         this.locale = locale;
         resource_Bundle = ResourceBundle.getBundle(resource_Name, locale);
     }

	/**
	 * @return the message_Formatter
	 */
	public MessageFormat getMessage_Formatter() {
		return message_Formatter;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}
 }
