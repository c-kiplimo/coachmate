package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response,Long> {
}
