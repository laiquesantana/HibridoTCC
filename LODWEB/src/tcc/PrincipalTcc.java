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
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.tika.sax.xpath.Matcher;

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

		int quantidadeUsuario = dbFunctions.getQtdAllUsers();
		System.out.println(quantidadeUsuario);
		for (int j = 1; j < 100; j++) {
			DBFunctions teste = new DBFunctions();

			List<Ratings> lista = new ArrayList<Ratings>();

			// List<Movies> listaMovies = new ArrayList<Movies>();

			/*
			 * for (Movies obj : listaDeFilmesNaoAvaliados) { //
			 * System.out.println(obj.getTitle()); }
			 */

			// pegar todos os filmes avaliados com nota maior que >=4
			ArrayList<Ratings> listaDeFilme = teste.getFilmes(j);

			// pega todos os filmes não avaliados independente da nota
			ArrayList<Movies> listaDeFilmesNaoAvaliados = teste.getFilmesNaoAvaliados(j);

			ArrayList<Movies> listaMovies = new ArrayList<Movies>();
			/*
			 * for (Ratings obj : listaDeFilme) { System.out.println (
			 * "id do filme: " +obj.getMovie_id() + ": nota: " + obj.getRating()
			 * ); }
			 */

			// para cada filme avaliado retorna em listaMovies o objeto Movies
			// com descrição , titulo etc...
			for (Ratings obj : listaDeFilme) {
				ArrayList<Movies> filme = teste.getDescription(obj.getMovie_id());
				listaMovies.addAll(filme);
				// System.out.println ( "id do filme: " +obj.getMovie_id() + ":
				// nota: " + obj.getRating() );
			}
			System.out.println("SIMILARIDADE DE FILMES PARA O USUARIO " + j + " ----------------");
			System.out.println(
					"----------------------------- similaridade coseno do filmes avaliados com os filmes não avaliado --------");

			for (Movies obj : listaMovies) {
				for (int i = 0; i < listaDeFilmesNaoAvaliados.size(); i++) {
					// ArrayList<Movies> listaMovies =
					// teste.getDescription(obj.getMovie_id());
					// System.out.println ( " \ntitulo => " + obj.getTitle()+
					String string1 = obj.getDescription();
					String string2 = listaDeFilmesNaoAvaliados.get(i).getDescription();

					float result1 = (float) LuceneCosineSimilarity.getCosineSimilarity(string1, string2);
					if (result1 > 0.2) {
						System.out.println("Calculo sem RETIRAR caractere especiais");
						System.out.println(result1);
						System.out.println("------------------------------------------");
					}

				
					float result2 = (float) LuceneCosineSimilarity.getCosineSimilarity(removeSpecialCharacters(string1),
							string2);
					if (result2 > 0.2) {
						System.out.println("UTILIZANDO FUNÇÃO removeSpecialCharacters() - Laique");
						System.out.println(result2);
						System.out.println("------------------------------------------");
					}
				

				
					float result3 = (float) LuceneCosineSimilarity
							.getCosineSimilarity(StringUtilsNode.removeInvalidCharacteres(string1), string2);
					if (result3 > 0.2) {
						System.out.println("UTILIZANDO FUNÇÃO removeInvalidCharacteres() - LODWEB");
						System.out.println(result3);
						System.out.println("------------------------------------------");
					}
				

					
					float result4 = (float) LuceneCosineSimilarity.getCosineSimilarity(
							StringUtilsNode.removeInvalidCharacteres(removeWords(string1)), removeWords(string2));
					if (result4 > 0.2) {
						System.out.println("UTILIZANDO FUNÇÃO removeWords() - laique");
						System.out.println(result4);
						System.out.println("------------------------------------------");
						System.out.println("o filme \n\n" + obj.getTitle() + "com o filme \n"
								+ listaDeFilmesNaoAvaliados.get(i).getTitle() + " possui similaridade:\n "
								+ (result4 * 100));
						teste.insertOrUpdateSimilarity(j, obj.getId(), listaDeFilmesNaoAvaliados.get(i).getId(),
								result4);
					}
			

					/*Float Resultado = (float) LuceneCosineSimilarity.getCosineSimilarity(string1, string2);
					if (Resultado > 0.2) {
						System.out.println("o filme \n\n" + obj.getTitle() + "com o filme \n"
								+ listaDeFilmesNaoAvaliados.get(i).getTitle() + " possui similaridade:\n "
								+ (Resultado * 100));
						teste.insertOrUpdateSimilarity(j, obj.getId(), listaDeFilmesNaoAvaliados.get(i).getId(),
								Resultado);
					}*/

				}

			}

			System.out.println(
					"-----------------------------------------------------------------------------------------------------------------------");
		}

	}

	public static String removeSpecialCharacters(String stringFonte) {
		String passa = stringFonte;

		passa = passa.replaceAll("[-+=*&%$#@!_ˈ:;ˌ]", "");
		passa = passa.replaceAll("['\"]", "");
		passa = passa.replaceAll("[<>()\\{\\}]", "");
		passa = passa.replaceAll("['\\\\.,()|/]", "");
		passa = passa.replaceAll("\\[", "");
		passa = passa.replaceAll("\\]", "");

		return passa;
	}

	public static boolean hasInvalidCharacter(String string) {
		String regex = "[|\"&*=+'@#$\\%\\/?{}?:;<>,\u00C0\u00C1\u00C2\u00C3\u00C4\u00C5\u00C6\u00C7\u00C8\u00C9\u00CA\u00CB\u00CC\u00CD\u00CE\u00CF\u00D0\u00D1\u00D2\u00D3\u00D4\u00D5\u00D6\u00D8\u0152\u00DE\u00D9\u00DA\u00DB\u00DC\u00DD\u0178\u00E0\u00E1\u00E2\u00E3\u00E4\u00E5\u00E6\u00E7\u00E8\u00E9\u00EA\u00EB\u00EC\u00ED\u00EE\u00EF\u00F0\u00F1\u00F2\u00F3\u00F4\u00F5\u00F6\u00F8\u0153\u00DF\u00FE\u00F9\u00FA\u00FB\u00FC\u00FD\u00FF]";
		Pattern p = Pattern.compile(regex);
		java.util.regex.Matcher m = p.matcher(string);
		return ((java.util.regex.Matcher) m).find();
	}

	public static String removeInvalidCharacteres(String string) {
		String corretString = string;
		if (hasInvalidCharacter(string)) {
			String regex = "[|\"&*=+'@#$%\\/?{}?:;_~<>,\u00C0\u00C1\u00C2\u00C3\u00C4\u00C5\u00C6\u00C7\u00C8\u00C9\u00CA\u00CB\u00CC\u00CD\u00CE\u00CF\u00D0\u00D1\u00D2\u00D3\u00D4\u00D5\u00D6\u00D8\u0152\u00DE\u00D9\u00DA\u00DB\u00DC\u00DD\u0178\u00E0\u00E1\u00E2\u00E3\u00E4\u00E5\u00E6\u00E7\u00E8\u00E9\u00EA\u00EB\u00EC\u00ED\u00EE\u00EF\u00F0\u00F1\u00F2\u00F3\u00F4\u00F5\u00F6\u00F8\u0153\u00DF\u00FE\u00F9\u00FA\u00FB\u00FC\u00FD\u00FF]";
			Pattern p = Pattern.compile(regex);
			java.util.regex.Matcher m = p.matcher(string);
			corretString = m.replaceAll("");
			// System.out.println(corretString);
		}

		corretString = corretString.replace("\\", "");

		return corretString;
	}

	public static String removeWords(String a) {

		String word;

		word = a;
		word = word.replaceAll("the", "");
		word = word.replaceAll("of", "");
		word = word.replaceAll("do", "");
		word = word.replaceAll("is", "");
		word = word.replaceAll("the", "");
		word = word.replaceAll("A", "");
		word = word.replaceAll("by", "");
		word = word.replaceAll("and", "");
		word = word.replaceAll("both", "");
		word = word.replaceAll("in", "");
		word = word.replaceAll("only", "");
		word = word.replaceAll("to", "");
		word = word.replaceAll("for", "");
		word = word.replaceAll("from", "");
		word = word.replaceAll("in", "");
		word = word.replaceAll("as", "");
		word = word.replaceAll("has", "");
		word = word.replaceAll("had", "");
		word = word.replaceAll("with", "");
		word = word.replaceAll("an", "");

		return word;
	}

}
