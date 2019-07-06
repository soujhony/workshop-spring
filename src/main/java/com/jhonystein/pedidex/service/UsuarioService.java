package com.jhonystein.pedidex.service;

import com.jhonystein.pedidex.model.Perfil;
import com.jhonystein.pedidex.model.Session;
import com.jhonystein.pedidex.model.Usuario;
import com.jhonystein.pedidex.repository.PerfilRepository;
import com.jhonystein.pedidex.repository.UsuarioRepository;
import com.jhonystein.pedidex.security.JwtTokenService;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuRepo;
    @Autowired
    private PerfilRepository perfilRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Session login(Usuario usuario) {
        Objects.requireNonNull(usuario.getEmail());
        Objects.requireNonNull(usuario.getSenha());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuario.getEmail(), usuario.getSenha())
        );

        Usuario base = usuRepo.findByEmail(usuario.getEmail());

        return new Session(base.getEmail(), jwtTokenService.generateToken(base));
    }

    public Session register(Usuario usuario) {
        Perfil userRole = perfilRepo.findByNome("USER");
        if (userRole == null) {
            userRole = new Perfil();
            userRole.setNome("USER");
            perfilRepo.save(userRole);
        }

        usuario.setPerfis(Arrays.asList(userRole));
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        Usuario base = usuRepo.save(usuario);

        return new Session(base.getEmail(), jwtTokenService.generateToken(base));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return toUserDetails(usuRepo.findByEmail(username));
    }

    private UserDetails toUserDetails(Usuario usu) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return usu.getPerfis().stream().map((perfil) -> {
                    return (GrantedAuthority) () -> perfil.getNome();
                }).collect(Collectors.toList());
            }

            @Override
            public String getPassword() {
                return usu.getSenha();
            }

            @Override
            public String getUsername() {
                return usu.getEmail();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

}
