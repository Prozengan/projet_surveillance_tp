package com.surveillance.tp.dao;

import static com.surveillance.tp.dao.DAOUtilitaire.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.surveillance.tp.beans.Examen;

public class DAOExamenImpl implements DAOExamen {

	private DAOFactory daoFactory;

	DAOExamenImpl( DAOFactory daoFactory ) {
		this.daoFactory = daoFactory;
	}

	/*
	 * Simple méthode utilitaire permettant de faire la correspondance (le
	 * mapping) entre une ligne issue de la table des utilisateurs (un
	 * ResultSet) et un bean Utilisateur.
	 */
	private static Examen map( ResultSet resultSet ) throws SQLException {
		Examen examen = new Examen();

		examen.setIdExam(resultSet.getInt("id"));
		examen.setIdProf(resultSet.getInt("id_user"));
		examen.setNomExam(resultSet.getInt("nom"));

		return examen;
	}

	//TEST (a mettre dans utilisateur)
	//private static final String SQL_INSERT_TEST_ETUD = "INSERT INTO Utilisateur (id, prenom, nom, numero_etudiant) VALUES (?, ?, ?, ?)";
	
	//TEST
	private static final String SQL_INSERT_TEST_EXAM = "INSERT INTO Examen (id, id_user, nom) VALUES (?, ?, ?)";
		
	@Override
	public void creer(Examen examen) throws DAOException {
		Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet valeursAutoGenerees = null;
	    try {
	        /* Récupération d'une connexion depuis la Factory */
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT_TEST_EXAM, true, examen.getIdExam(), examen.getIdProf(), examen.getNomExam());
	        int statut = preparedStatement.executeUpdate();
	        /* Analyse du statut retourné par la requête d'insertion */
	        if ( statut == 0 ) {
	            throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
	        }
	        /* Récupération de l'id auto-généré par la requête d'insertion */
	        valeursAutoGenerees = preparedStatement.getGeneratedKeys();
	        if ( valeursAutoGenerees.next() ) {
	            /* Puis initialisation de la propriété id du bean Utilisateur avec sa valeur */
	            //examen.setIdExam( valeursAutoGenerees.getLong( 1 ) );
	        } else {
	            throw new DAOException( "Échec de la création de l'utilisateur en base, aucun ID auto-généré retourné." );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
	    }
	}
	
	//TMP
	private static final String SQL_SELECT_TEST_EXAM = "SELECT id, id_user, nom FROM Examen WHERE nom = ?";
		
	@Override
	public Examen trouver(int nomExam) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Examen examen = null;

		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_TEST_EXAM, false, nomExam );
			resultSet = preparedStatement.executeQuery();
			/* Parcours de la ligne de données de l'éventuel ResulSet retourné */
			if ( resultSet.next() ) {
				examen = map( resultSet );
			}
		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			fermeturesSilencieuses( resultSet, preparedStatement, connexion );
		}
		return examen;
	}

	@Override
	public void supprimer(Examen examen) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void miseAJour(Examen examen) throws DAOException {
		// TODO Auto-generated method stub
		
	}

}
