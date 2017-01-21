package sec.project.repository;

import sec.project.domain.Signup;

import java.util.List;

public interface SignupRepositoryCustom {

    List<Signup> findByOwner(String owner);
}