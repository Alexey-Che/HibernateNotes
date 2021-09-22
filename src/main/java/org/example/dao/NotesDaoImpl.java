package org.example.dao;

import org.example.models.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class NotesDaoImpl implements NotesDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public NotesDaoImpl(EntityManagerFactory manager) {
        this.entityManager = manager.createEntityManager();
    }

    @Override
    @Transactional
    public void addNote(Note note) {
        entityManager.persist(note);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> showAllNotes() {
        return entityManager.createQuery("select n from Note n",
                Note.class).getResultList();
    }

    @Override
    @Transactional
    public List<Note> searchBySubstring(String substring) {
        String search = "%" + substring.toLowerCase() + "%";
        return entityManager.createQuery("select n from Note n where lower(n.title) like :string or lower(n.text) like :string",
                Note.class).setParameter("string", search).getResultList();
    }

    @Override
    @Transactional
    public void deleteNote(Long id) {
        entityManager.remove(entityManager.find(Note.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public Note showNoteById(Long id) {
        return entityManager.find(Note.class, id);
    }

    @Override
    @Transactional
    public void update(Long id, Note note) {
        Note noteToBeUpdated = entityManager.find(Note.class, id);
        noteToBeUpdated.setTitle(note.getTitle());
        noteToBeUpdated.setText(note.getText());
    }
}
