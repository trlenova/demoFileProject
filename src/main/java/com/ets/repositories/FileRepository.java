package com.ets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ets.modals.File;


public interface FileRepository extends JpaRepository<File, Long> {

}
