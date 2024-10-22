package com.tizo.br.services;

import com.tizo.br.exceptions.ResourceNotFoundException;
import com.tizo.br.mapper.ModelMapperUtil;
import com.tizo.br.model.Permission;
import com.tizo.br.model.User;
import com.tizo.br.model.vo.security.UserAccountRecordVO;
import com.tizo.br.model.vo.security.UserInfosVO;
import com.tizo.br.repositories.PermissionRepository;
import com.tizo.br.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class UserService implements UserDetailsService {

    private final Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    UserRepository userRepository;

    @Autowired
    PermissionRepository permissionRepository;

    public List<UserInfosVO> findAllUsers() {
        return ModelMapperUtil.parseListObjects(userRepository.findAll(), UserInfosVO.class);
    }

    public UserInfosVO createUser(UserAccountRecordVO user, List<String> authorityID) {

        return ModelMapperUtil.parseObject(userRepository.save(userAccountRecordVOToUser(user, authorityID)), UserInfosVO.class);
    }

    public void delete(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No user found for this id."));
        userRepository.delete(user);
    }

    private User userAccountRecordVOToUser(UserAccountRecordVO userAccountRecordVO, List<String> authorityID) {

        User user = new User();

        //Criptografa a senha antes de salvar no banco de dados
        Map<String, PasswordEncoder> encoderMap = new HashMap<>();
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
                //Senha, ps: senha temporária
                "tizo",
                //Tamanho do valor aleatório aplicado em cada interação
                8,
                //Número de interações
                185000,
                //Algoritmo de encriptação
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        encoderMap.put("pbkdf2", pbkdf2PasswordEncoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoderMap);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);


        //Atualiza a senha para a senha criptografada
        user.setPassword(passwordEncoder.encode(userAccountRecordVO.password()));
        user.setEmail(userAccountRecordVO.email());
        user.setUsername(userAccountRecordVO.username());

        List<Permission> permissions = new ArrayList<>();

        for (String authority : authorityID) {
            permissions.add(permissionRepository.findByDescription(authority));
        }

        user.setPermissions(permissions);

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        var user = userRepository.findByEmail(email);

        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException(email + " not found!");
        }
    }
}
