package tcc;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.ResourceImpl;

import cosinesimilarity.LuceneCosineSimilarity;
import database.DBFunctions;
import model.HitRate;
import model.Movies;
import model.OnlineEvaluation;
import model.Ratings;
import model.User;
import node.Classifier;
import node.Evaluation;
import node.IConstants;
import node.Item;
import node.Lodica;
import node.Node;
import node.NodePrediction;
import node.NodeUtil;
import node.SparqlWalk;
import parser.Parser;
import util.StringUtilsNode;
public class PrincipalTcc {

	public static void main(String[] args) {

		DBFunctions dbFunctions = new DBFunctions();
		dbFunctions.analyseOnlineEvaluationByUser(IConstants.LDSD_LOD);

		int quantidadeUsuario= dbFunctions.getQtdAllUsers();
		System.out.println(quantidadeUsuario);
		for (int j = 1; j < quantidadeUsuario; j++) {
			DBFunctions teste = new DBFunctions();

			List<Ratings> lista = new ArrayList<Ratings>();

			// List<Movies> listaMovies = new ArrayList<Movies>();
			

			/*for (Movies obj : listaDeFilmesNaoAvaliados) {
				// System.out.println(obj.getTitle());
			}*/
			
			// pegar todos os filmes avaliados com nota maior que >=4
			ArrayList<Ratings> listaDeFilme = teste.getFilmes(j);
			
			//pega todos os filmes não avaliados independente da nota
			ArrayList<Movies> listaDeFilmesNaoAvaliados = teste.getFilmesNaoAvaliados(j);
			
			
			ArrayList<Movies> listaMovies = new ArrayList<Movies>();
		/*	
			 * for (Ratings obj : listaDeFilme) { System.out.println (
			 * "id do filme: " +obj.getMovie_id() + ": nota: " + obj.getRating()
			 * ); }
			 */
			
			// para cada filme avaliado retorna em listaMovies o objeto  Movies com descrição , titulo etc...
			for (Ratings obj : listaDeFilme) {
				ArrayList<Movies> filme = teste.getDescription(obj.getMovie_id());
				listaMovies.addAll(filme);
				// System.out.println ( "id do filme: " +obj.getMovie_id() + ":
				// nota: " + obj.getRating() );
			}
			System.out.println("SIMILARIDADE DE FILMES PARA O USUARIO " + j + " ----------------");
			System.out.println(	"----------------------------- similaridade coseno do filmes avaliados com os filmes não avaliado --------");

			for (Movies obj : listaMovies) {
				for (int i = 0; i < listaDeFilmesNaoAvaliados.size(); i++) {
					// ArrayList<Movies> listaMovies =
					// teste.getDescription(obj.getMovie_id());
					// System.out.println ( " \ntitulo => " + obj.getTitle()+
					// "\ndescricao => " +obj.getDescription() );
					Float Resultado = (float) LuceneCosineSimilarity.getCosineSimilarity(obj.getDescription(),
							listaDeFilmesNaoAvaliados.get(i).getDescription());
					if (Resultado > 0.2) {
						System.out.println("o filme \n\n" + obj.getTitle() + "com o filme \n"
								+ listaDeFilmesNaoAvaliados.get(i).getTitle() + " possui similaridade:\n "
								+ (Resultado * 100));
						teste.insertOrUpdateSimilarity( j, obj.getId(), listaDeFilmesNaoAvaliados.get(i).getId(), Resultado);
					}

				}

			}
			
			System.out.println("-----------------------------------------------------------------------------------------------------------------------");
		}
		

	}

}
