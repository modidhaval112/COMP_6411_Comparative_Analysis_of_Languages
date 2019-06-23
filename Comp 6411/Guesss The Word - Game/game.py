# File: Game.py


class game(object):
    """ This class represent the game object for each played game"""

    def __init__(self, game, word, status, badGuesses, missedLetters, score):
        """
        this method will store data for the game object in respective attributes
        :param game: game index
        :param word: randomly chosen word
        :param status: status if player guessed it correctly or gave up
        :param badGuesses: no of times player guessed the wrong words
        :param missedLetters: no of times player guessed the wrong letters
        :param score: counted score
        """
        self.game = game
        self.word = word
        self.status = status
        self.badGuesses = badGuesses
        self.missedLetters = missedLetters
        self.score = score