import collections

import cv2 as cv
import numpy as np

from ImageUtilities import utils
from WebcamAnalyzer import Analyzer


class ImageManipulator:
    playerIsWhite = Analyzer.determine_player_color()

    column_list = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h']
    row_list = [1, 2, 3, 4, 5, 6, 7, 8]
    if playerIsWhite:
        row_list.reverse()

    # corners = [(224, 56), (1048, 44), (97, 905), (1196, 892)]
    # corners = [(232, 43), (1059, 46), (87, 883), (1186, 893)]
    corners = [(), (), (), ()]

    square_dictionary = collections.OrderedDict()

    def __init__(self, img):
        self.img = img
        self.blank_img = utils.gray(utils.create_blank())
        # squares list that will hold contours
        self.squares = []
        # squares list that will hold square locations
        self.square_array = [[(c + str(r))[:] for c in self.column_list] for r in self.row_list]

    def draw_contours(self, high_thresh=False, p_thresh=None, c_thresh=None):
        self.squares = Analyzer.find_contours(self.blank_img)
        counter = 0
        # squares_occupied = 0
        for row in range(len(self.square_array)):
            for col in range(len(self.square_array[0])):
                approx = self.squares[counter]
                self.square_dictionary[self.square_array[row][col]] = False
                if high_thresh:
                    if Analyzer.is_occupied(utils.get_contour_image(self.img, approx), p_thresh, c_thresh):
                        self.img = cv.drawContours(self.img, [approx], 0, (0, 0, 255), 3)
                        self.square_dictionary[str(self.square_array[row][col])] = True
                        # squares_occupied += 1
                    else:
                        self.img = cv.drawContours(self.img, [approx], 0, (0, 255, 0), 3)
                else:
                    if Analyzer.is_occupied(utils.get_contour_image(self.img, approx)):
                        self.img = cv.drawContours(self.img, [approx], 0, (0, 0, 255), 3)
                        self.square_dictionary[str(self.square_array[row][col])] = True
                        # squares_occupied += 1
                    else:
                        self.img = cv.drawContours(self.img, [approx], 0, (0, 255, 0), 3)
                counter += 1
        # print(squares_occupied)
        # if squares_occupied > 32:
        #    self.draw_contours(True, p_thresh=.75, c_thresh=60)
        # elif squares_occupied < 32:
        #   self.draw_contours(True, p_thresh=.75, c_thresh=50)

    def label_squares(self, s=None, font=cv.FONT_HERSHEY_SIMPLEX):
        if len(self.squares) != len(self.square_array) * len(self.square_array[0]):
            return
        counter = 0
        if s is not None:
            cv.putText(self.img, str(counter), utils.find_center(s), font, .5, (0, 0, 255), 2, cv.LINE_AA)
            return
        for r in range(len(self.square_array)):
            for c in range(len(self.square_array[0])):
                cv.putText(self.img, self.square_array[r][c], utils.find_center(self.squares[counter]), font, .5,
                           (0, 0, 255), 2, cv.LINE_AA)
                counter += 1

    def draw_grid(self, color=(255, 255, 255), line_thickness=10, line_type=cv.LINE_AA):
        step = int(self.img.shape[0] / 8)
        s = 2
        for i in range(9):
            cv.line(self.blank_img, (s, 0), (s, self.blank_img.shape[0]), color, line_thickness, line_type)
            cv.line(self.blank_img, (0, s), (self.blank_img.shape[1], s), color, line_thickness, line_type)
            s += step

    def warp(self):
        from_points = np.float32([self.corners[0], self.corners[1], self.corners[2], self.corners[3]])
        to_points = np.float32([[0, 0], [700, 0], [0, 700], [700, 700]])
        m = cv.getPerspectiveTransform(from_points, to_points)
        self.img = cv.warpPerspective(self.img, m, (700, 700))

    def get_image(self):
        return self.img

    def get_blank_image(self):
        return self.blank_img

    def update_frame(self, frame):
        self.img = frame

    def set_corners(self, event, x, y, flags, param):
        if event == cv.EVENT_LBUTTONDOWN:
            for i in range(len(self.corners)):
                if len(self.corners[i]) == 0:
                    self.corners[i] = (x, y)
                    break

    def draw_corner_circles(self, frame):
        for c in self.corners:
            if len(c) != 2:
                continue
            cv.circle(frame, (c[0], c[1]), 5, (0, 0, 255), -1)
        return frame

    def are_corners_not_set(self):
        corners = self.corners[:]
        if len([corner for corner in corners if len(corner) == 0]) > 0:
            return True
        print(self.corners)
        return False

    def write_to_file(self):
        writer = open('/Users/aaronyoung/Coding/Chess6/src/PythonCommunicator/board.txt', 'w')
        for key, value in self.square_dictionary.items():
            writer.write(str(key) + ' ' + str(value) + '\n')
        writer.close()
