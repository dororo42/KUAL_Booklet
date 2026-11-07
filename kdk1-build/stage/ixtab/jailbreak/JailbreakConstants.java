/* This product is released by Ixtab under the terms of the WTFPL. See LICENSE.txt for more details. */

package ixtab.jailbreak;

public interface JailbreakConstants {
	
	int VERSION = 2011051901;
	
	int JAILBREAK = 0xdead;
	int MY = 0xbabe;
	String KINDLE = "kindle";
	String JAILBREAKER = "jailbreaker";
	
	
	interface Protocol {
		String COMMAND = "command";
		
		String PROTECTION_DOMAIN = "protectionDomain";
		String PROTECTION_DOMAIN_CLASS = "protectionDomain.class";
		
		String POLICY = "policy";
		String POLICY_REPLACE_WITH = "policy.replaceWith";
		
		String CLASSLOADER = "classLoader";
		String CLASSLOADER_PARENT = "classLoader.parent";
		
		String VERSION = "version";
	}
}
