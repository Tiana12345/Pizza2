package com.accenture.service.serviceimpl;


import com.accenture.exception.ClientException;
import com.accenture.repository.dao.ClientDao;
import com.accenture.repository.entity.Client;
import com.accenture.service.dto.ClientDto;
import com.accenture.service.mapper.ClientMapper;
import com.accenture.service.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    public static final String ID_N_EXISTE_PAS = "L'id n'existe pas";
    private final ClientDao dao;
    private final ClientMapper mapper;

    public ClientServiceImpl(ClientDao dao, ClientMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public ClientDto ajouter(ClientDto clientDto) {
        verifierClientDto(clientDto);
        Client client = mapper.toClient(clientDto);
        Client clientSave = dao.save(client);
        return mapper.toClientDto(client);
    }


    @Override
    public List<ClientDto> trouverTous() {
        return dao.findAll().stream()
                .map(mapper::toClientDto)
                .toList();
    }

    @Override
    public ClientDto trouverByEmail(String email) throws EntityNotFoundException {
        Optional<Client> optClient = dao.findById(email);
        if (optClient.isEmpty())
            throw new EntityNotFoundException("Ce mail n'existe pas dans notre base de données");
        Client client = optClient.get();
        return mapper.toClientDto(client);
    }

    @Override
    public ClientDto modifier(ClientDto clientDto) throws ClientException, EntityNotFoundException {
        if (clientDto == null)
            throw new ClientException("Le client ne peut pas être nul");
        String email = clientDto.email();
        Optional<Client> optClient = dao.findById(email);
        if (optClient.isEmpty())
            throw new EntityNotFoundException(ID_N_EXISTE_PAS);
        Client clientExist = optClient.get();
        Client nouvClient = mapper.toClient(clientDto);
        remplacer(clientExist, nouvClient);
        Client clientEnreg = dao.save(clientExist);
        return mapper.toClientDto(clientEnreg);
    }

    @Override
    public void supprimer(String email) throws EntityNotFoundException {
        if (dao.existsById(email))
            dao.deleteById(email);
        else
            throw new EntityNotFoundException(ID_N_EXISTE_PAS);
    }

    private static void verifierClientDto(ClientDto clientDto) {
        if (clientDto == null)
            throw new ClientException("Le client doit exister");
        if (clientDto.nom() == null || clientDto.nom().isBlank())
            throw new ClientException("Le nom du client doit être renseigné");
        verifierEmail(clientDto.email());

    }

    private static void remplacer(Client clientExist, Client nouvClient) {
        if (nouvClient.getNom() != null && !nouvClient.getNom().isBlank())
            clientExist.setNom(nouvClient.getNom());
    }

    private static void verifierEmail(String email) throws ClientException {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email == null || email.isBlank())
            throw new ClientException("L'email doit être renseigné");
        if (!email.matches(emailRegex))
            throw new ClientException("L'email est obligatoire et doit être écrit en format email");
    }
}
