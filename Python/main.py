import cv2 as cv

from ImageUtilities import utils
from BoardRecognition.ImageManipulator import ImageManipulator
from WebcamAnalyzer import Analyzer


def image(manipulation=None):
    img = cv.imread('Images/occupiedFrame.png')
    cv.imshow('img', img)
    image_manipulator = ImageManipulator(img)
    cv.namedWindow('img')
    cv.setMouseCallback('img', image_manipulator.set_corners)
    while image_manipulator.are_corners_not_set():
        cv.imshow('img', img)
        image_manipulator.draw_corner_circles(img)
        if cv.waitKey(15) == 113:
            break
    image_manipulator.warp()
    image_manipulator.draw_grid()
    cv.imshow('Blank Image With Grid', image_manipulator.get_blank_image())
    image_manipulator.draw_contours()
    image_manipulator.label_squares()
    cv.imshow('Original', img)
    cv.imshow('Manipulation', image_manipulator.get_image())

    k = cv.waitKey(0)
    if k == ord('r'):
        image(manipulation)
    cv.destroyAllWindows()
    exit()


def video():
    cam = cv.VideoCapture(0)
    last_frame = None
    first = True
    while True:
        ret, frame = cam.read()
        if not ret:
            continue
        elif last_frame is None:
            last_frame = frame
            continue
        image_manipulator = ImageManipulator(frame)
        if image_manipulator.are_corners_not_set():
            cv.namedWindow('Frame')
            cv.setMouseCallback('Frame', image_manipulator.set_corners)
            while image_manipulator.are_corners_not_set():
                cv.imshow('Frame', frame)
                ret, frame = cam.read()
                frame = image_manipulator.draw_corner_circles(frame)
                if cv.waitKey(15) == 113:
                    break
        else:
                cv.destroyWindow('Frame')
        if not first and Analyzer.motion_detected(last_frame, frame):
            print('Motion Detected')
            last_frame = frame
            continue
        image_manipulator.warp()
        image_manipulator.draw_grid()
        image_manipulator.draw_contours()
        image_manipulator.label_squares()
        image_manipulator.write_to_file()
        # cv.imshow('Original', frame)
        cv.imshow('Manipulation', image_manipulator.get_image())
        k = cv.waitKey(5)
        if k == ord('q'):
            break
        elif k == ord('s'):
            cv.imwrite('frame.png', frame)
        last_frame = frame
        first = False
    cv.destroyAllWindows()
    cam.release()
    exit()


utils.reset_text_files()
image()
