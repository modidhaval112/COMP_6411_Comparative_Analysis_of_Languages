# File: guess.py

import pickle
from stringDatabase import *
from game import *


class guess:
	"""
	this class represents the whole game. It includes menu
	and logic to count the total score at the end
	"""
	flag = True
	a = stringDatabase()
	word = a.chooseWord()
	tempWord = "----"
	tempWord1 = ""
	# print(word)
	print("\n\n  The Great Guessing Game ")
	print("***************************")

	missedLetters = 0
	badGuesses = 0
	output_list = []
	output_list1 = []
	k = 1
	object_list = []
	score = 0
	times_letter_asked = 0
	total_score = 0

	table = {'a': 8.17, 'c': 2.78, 'b': 1.49, 'e': 12.70, 'd': 4.25, 'g': 2.02, 'f': 2.23, 'i': 6.97, 'h': 6.09, 'k': 0.77, 'j': 0.15, 'm': 2.41, 'l': 4.03, 'o': 7.51, 'n': 6.75, 'q': 0.10, 'p': 1.93, 's': 6.33, 'r': 5.99, 'u': 2.76, 't': 9.06, 'w': 2.36, 'v': 0.98, 'y': 1.97, 'x': 0.15, 'z': 0.07}

	while flag:
		print("\n\nCurrent Guess: " + tempWord)
		print("g = guess, t = tell me, l for a letter, and q to quit")

		choice = input()

		if choice == 'g' :
			print("### Guess the word ")
			guessedWord = input()
			if guessedWord == word:
				print("### Congrats")

				for i, c in enumerate(tempWord):
					if c == '-':
						output_list1.extend(word[i])

				for letter in output_list1:
					score += table.get(letter)

				if times_letter_asked != 0:
					score = score / times_letter_asked

				for i in range(badGuesses):
					score = score * 0.9

				with open('game_data.pkl', 'wb') as output:
					total_score += score
					object_list.append(game(k, word, 'Success', badGuesses, len(output_list) + missedLetters, score))
					pickle.dump(game, output, pickle.HIGHEST_PROTOCOL)
					k += 1

				tempWord = "----"
				tempWord1 = ""
				missedLetters = 0
				output_list = []
				output_list1 = []
				badGuesses = 0
				times_letter_asked = 0
				score = 0
				word = a.chooseWord()
				# print(word)

			else:
				badGuesses += 1
				print("### Try again looser")
		elif choice == 't':

			for i, c in enumerate(tempWord):
				if c == '-':
					output_list.extend(word[i])
			print('### Chosen word was : ', word)
			# print("MissedLetters : " , output_list)

			for i, c in enumerate(tempWord):
				if c == '-':
					output_list1.extend(word[i])

			for letter in output_list1:
				score -= table.get(letter)

			with open('game_data.pkl', 'wb') as output:
				total_score += score
				object_list.append(game(k, word, 'Gave up', badGuesses, len(output_list) + missedLetters, score))
				pickle.dump(game, output, pickle.HIGHEST_PROTOCOL)
				k += 1

			tempWord = "----"
			tempWord1 = ""
			missedLetters = 0
			times_letter_asked = 0
			output_list = []
			output_list1 = []
			badGuesses = 0
			score = 0
			word = a.chooseWord()
			# print(word)

		elif choice == 'l':
			times_letter_asked += 1
			print("### Write a letter")
			letter = input()
			tempWord1 = tempWord
			if word.find(letter) == -1:
				missedLetters += 1
			if word.find(letter) != -1:
				for i, ltr in enumerate(word):
					if ltr == letter:
						tempList = list(tempWord)
						tempList[i] = letter
						tempWord = "".join(tempList)
			if tempWord.find('-') == -1:
				print("### Congrats")

				for i, c in enumerate(tempWord1):
					if c == '-':
						output_list1.extend(word[i])

				for letter in output_list1:
					score += table.get(letter)

				if times_letter_asked != 0:
					score = score / times_letter_asked

				for i in range(badGuesses):
					score = score * 0.9

				with open('game_data.pkl', 'wb') as output:
					total_score += score
					object_list.append(game(k, word, 'Success', badGuesses, len(output_list) + missedLetters, score))
					pickle.dump(game, output, pickle.HIGHEST_PROTOCOL)
					k += 1

				tempWord = "----"
				tempWord1 = ""
				missedLetters = 0
				times_letter_asked = 0
				output_list = []
				output_list1 = []
				badGuesses = 0
				score = 0
				word = a.chooseWord()
				# print(word)

		elif choice == 'q':

			with open('game_data.pkl', 'rb') as input:
				print("\n\nGame", "      ", "Word", "      ", "Status", "      ", "Bad Guesses", "       ", "Missed Letters", "      ", "Score")
				for game in object_list:
					print(game.game, "         ", game.word, "      ", game.status, "     ", game.badGuesses, "                 ", game.missedLetters, "                   ", "%.2f" % game.score)

			print("Total Score %.2f" % total_score , "\n")
			flag = False
		else:
			print("### Choose Valid Option")