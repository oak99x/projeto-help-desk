package mfreitas.msoauth.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import feign.FeignException;
import mfreitas.msoauth.exceptions.FailedToRespondException;
import mfreitas.msoauth.exceptions.ObjectNotFoundException;
import mfreitas.msoauth.feignClients.UserFeign;
import mfreitas.msoauth.models.User;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserFeign userFeign;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userFeign.getUserByEmail(username).getBody();
            return user;
        } catch (FeignException.NotFound ex) {
            throw new ObjectNotFoundException(
                    messageSource.getMessage("email.not.found", null, null) + ex.getMessage());
        } catch (UsernameNotFoundException ex) {
            throw new ObjectNotFoundException(
                    messageSource.getMessage("email.not.found", null, null) + ex.getMessage());
        } catch (Exception ex) {
            throw new FailedToRespondException(
                    messageSource.getMessage("failed.to.respond", null, null) + ex.getMessage());
        }
    }
}
