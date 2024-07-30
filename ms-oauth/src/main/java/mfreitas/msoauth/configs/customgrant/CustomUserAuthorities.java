package mfreitas.msoauth.configs.customgrant;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

public class CustomUserAuthorities {

	private UUID id;
	private String username;
	private Collection<? extends GrantedAuthority> authorities;
	private String mode;

	public CustomUserAuthorities(UUID id, String username, Collection<? extends GrantedAuthority> authorities, String mode) {
		this.id = id;
		this.username = username;
		this.authorities = authorities;
		this.mode = mode;
	}

	public UUID getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getMode() {
		return mode;
	}
}
