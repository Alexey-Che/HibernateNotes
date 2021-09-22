package org.example.dao;

import org.example.models.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
@Transactional
public class NotesDaoImpl implements NotesDao {

    private final EntityManagerFactory manager;

    @Autowired
    public NotesDaoImpl(EntityManagerFactory manager) {
        this.manager = manager;
    }

    @Override
    @Transactional
    public void addNote(Note note) {
        EntityManager entityManager = manager.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(note);
        entityManager.getTransaction().commit();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> showAllNotes() {
        EntityManager entityManager = manager.createEntityManager();
        return entityManager.createQuery("select n from Note n",
                Note.class).getResultList();
    }

    @Override
    @Transactional
    public List<Note> searchBySubstring(String substring) {
        EntityManager entityManager = manager.createEntityManager();
        String search = "%" + substring.toLowerCase() + "%";
        return entityManager.createQuery("select n from Note n where lower(n.title) like :string or lower(n.text) like :string",
                Note.class).setParameter("string", search).getResultList();
    }

    @Override
    @Transactional
    public void deleteNote(Long id) {
        EntityManager entityManager = manager.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(Note.class, id));
        entityManager.getTransaction().commit();
    }

    @Override
    @Transactional(readOnly = true)
    public Note showNoteById(Long id) {
        EntityManager entityManager = manager.createEntityManager();
        return entityManager.find(Note.class, id);
    }

    @Override
    @Transactional
    public void update(Long id, Note note) {
        EntityManager entityManager = manager.createEntityManager();
        entityManager.getTransaction().begin();
        Note noteToBeUpdated = entityManager.find(Note.class, id);
        noteToBeUpdated.setTitle(note.getTitle());
        noteToBeUpdated.setText(note.getText());
        entityManager.getTransaction().commit();
    }
}
