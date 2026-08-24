package com.backend.hexagonal.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.backend.hexagonal.application.port.in.CreateUserUseCase;
import com.backend.hexagonal.application.port.in.DeleteUserUseCase;
import com.backend.hexagonal.application.port.in.GetAllUserUseCase;
import com.backend.hexagonal.application.port.in.GetByIdUserUseCase;
import com.backend.hexagonal.application.port.in.UpdateUserUseCase;
import com.backend.hexagonal.application.port.out.UserPersistencePort;
import com.backend.hexagonal.application.service.CreateUserService;
import com.backend.hexagonal.application.service.DeleteUserService;
import com.backend.hexagonal.application.service.GetAllUserService;
import com.backend.hexagonal.application.service.GetByIdUserService;
import com.backend.hexagonal.application.service.UpdateUserService;

@Configuration
public class BeanConfiguration {

    @Bean
    public CreateUserUseCase createUserUseCase(UserPersistencePort userPersistencePort) {
        return new CreateUserService(userPersistencePort);
    }

    @Bean
    public GetAllUserUseCase getAllUserUseCase(UserPersistencePort userPersistencePort) {
        return new GetAllUserService(userPersistencePort);
    }

    @Bean
    public GetByIdUserUseCase getByIdUserUseCase(UserPersistencePort userPersistencePort) {
        return new GetByIdUserService(userPersistencePort);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(UserPersistencePort userPersistencePort) {
        return new UpdateUserService(userPersistencePort);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserPersistencePort userPersistencePort) {
        return new DeleteUserService(userPersistencePort);
    }
}
