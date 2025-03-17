package bg.softuni.dam.dam_advertisements.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    UserDetails extractUserInformation(String jwtToken);
}
