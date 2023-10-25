package com.instargram101.starcard.repoository;

import com.instargram101.starcard.entity.Starcard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarcardRepository extends JpaRepository<Starcard, Long> {

}
