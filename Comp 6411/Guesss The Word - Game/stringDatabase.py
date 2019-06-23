# File: StringDatabase.py
import random


class stringDatabase:
	"""
	This class mostly do i/o operation on file. It will read data and pass it to other class.
	"""
	def chooseWord (self):
		"""
		chooseword method will chose a random word from the 'four_letters.txt' file
		:return: it will return the randomly chosen word
		"""
		word_list = []
		with open("four_letters.txt", "r") as f:
			for line in f:
				word_list.extend(line.split())
		return word_list[random.randint(0, 4029)];

