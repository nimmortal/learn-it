package it.learn.jacket.services;

import it.learn.jacket.models.JacketEntry;
import it.learn.repository.Repository;

import java.util.List;

public class JacketEntryServiceOnRepository implements JacketEntryService {
    private Repository repository;

    public JacketEntryServiceOnRepository(Repository jacketRepository) {
        this.repository = jacketRepository;
    }


    @Override
    public List<JacketEntry> getAllEntries() {
	return null;
    }
}

