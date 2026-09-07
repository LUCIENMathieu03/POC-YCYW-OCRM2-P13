package com.poc_ycyw.ycyw.repository;

import com.poc_ycyw.ycyw.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    // Ici on ne peut pas se contenter du nommage automatique (findBy...) car on a besoin
    // d'un OR entre deux colonnes différentes : on écrit donc la requête en JPQL
    // (proche du SQL, mais qui manipule les entités Java "Message" et leurs champs,
    // pas directement les noms de tables/colonnes SQL).
    // Reconstitue toute la conversation d'un client donné : tous les messages où
    // ce client est soit l'expéditeur (utilisateurId), soit le destinataire, triés par date.
    @Query("SELECT m FROM Message m WHERE m.utilisateurId = :clientId OR m.destinataire = :clientId ORDER BY m.dateEnvoi ASC")
    List<Message> findConversationWithClient(@Param("clientId") Integer clientId);

    // Un message envoyé par un client a toujours destinataire = NULL
    // On récupère donc les id des clients distincts ayant écrit au moins un message,
    // pour construire la liste des conversations côté vue admin.
    @Query("SELECT DISTINCT m.utilisateurId FROM Message m WHERE m.destinataire IS NULL")
    List<Integer> findDistinctClientIds();
}
