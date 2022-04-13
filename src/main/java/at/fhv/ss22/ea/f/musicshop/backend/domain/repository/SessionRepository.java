package at.fhv.ss22.ea.f.musicshop.backend.domain.repository;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;

import java.util.Optional;

public interface SessionRepository {

    void add(Session session);

    Optional<Session> sessionById(SessionId sessionId);

    void removeExpiredSessions();
}
